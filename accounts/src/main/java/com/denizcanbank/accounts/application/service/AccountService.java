package com.denizcanbank.accounts.application.service;

import com.denizcanbank.accounts.application.domain.entity.Account;
import com.denizcanbank.accounts.application.domain.entity.AccountEvent;
import com.denizcanbank.accounts.application.domain.exception.AccountAlreadyRegisteredException;
import com.denizcanbank.accounts.application.domain.exception.InternalErrorException;
import com.denizcanbank.accounts.application.domain.exception.NoSuchAccountException;
import com.denizcanbank.accounts.application.domain.repository.IAccountRepository;
import com.denizcanbank.accounts.application.domain.repository.IEventRepository;
import com.denizcanbank.accounts.application.domain.service.AccountConsistencyService;
import com.denizcanbank.accounts.application.domain.usecase.CrudUseCase;
import com.denizcanbank.accounts.application.domain.valueObject.AccountNumber;
import com.denizcanbank.accounts.application.domain.valueObject.EventName;
import com.denizcanbank.accounts.application.domain.valueObject.Payload;
import com.denizcanbank.accounts.application.domain.valueObject.SecurityNumber;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountService implements CrudUseCase {

    private final IEventRepository eventRepository;

    private final IAccountRepository repository;

    private final AccountConsistencyService accountConsistencyService;

    private final TransactionalOperator transactionalOperator;

    public AccountService(IEventRepository eventRepository,
            IAccountRepository repository,
                          AccountConsistencyService accountConsistencyService,
                          ReactiveTransactionManager transactionManager) {
        this.eventRepository = eventRepository;
        this.repository = repository;
        this.accountConsistencyService = accountConsistencyService;
        this.transactionalOperator = TransactionalOperator.create(transactionManager);
    }

    @Override
    public Mono<Account> registerAccount(Account account) {
        return transactionalOperator.transactional(
                repository.isAccountExists(account)
                        .flatMap(exists -> exists ? Mono.error(new AccountAlreadyRegisteredException("Account already registered: " + account.accountNumber())) : Mono.just(account))
                        .flatMap(repository::registerAccount)
                        .flatMap(registeredAccount -> {
                            var payload = new Payload(
                                    EventName.REGISTRATION.label(),
                                    registeredAccount.id().toString(),
                                    registeredAccount.securityNumber().toString(),
                                    registeredAccount.accountNumber().toString(),
                                    registeredAccount.accountType().name()
                            );

                            var event = new AccountEvent();
                            event.setName(EventName.REGISTRATION);
                            event.setAggregateId(registeredAccount.id());
                            event.setPayload(payload);
                            return eventRepository.saveAccountRegistrationEvent(event).thenReturn(registeredAccount);
                        })
                        .onErrorMap(DataAccessException.class, e -> new InternalErrorException(e.getMessage()))
        );
    }

    @Override
    public Mono<Account> updateAccount(Account account) {
        return fetchAccount(account.accountNumber())
                .map(fetchedAccount -> {
                    accountConsistencyService.checkConsistency(fetchedAccount, account);
                    account.id(fetchedAccount.id());
                    return account;
                })
                .flatMap(repository::updateAccount)
                .onErrorMap(DataAccessException.class, e -> new InternalErrorException(e.getMessage()));
    }

    @Override
    public Mono<Account> fetchAccount(AccountNumber accountNumber) {
        return repository.fetchAccount(accountNumber)
                .switchIfEmpty(Mono.error(new NoSuchAccountException("No such account with account number: " + accountNumber)))
                .onErrorMap(DataAccessException.class, e -> new InternalErrorException(e.getMessage()));
    }

    @Override
    public Flux<Account> fetchAccounts(SecurityNumber securityNumber) {
        return repository.fetchAccounts(securityNumber)
                .switchIfEmpty(Mono.error(new NoSuchAccountException("No such account with security number: " + securityNumber)))
                .onErrorMap(DataAccessException.class, e -> new InternalErrorException(e.getMessage()));
    }

    @Override
    public Mono<Void> deleteAccount(AccountNumber accountNumber) {
        return repository.fetchAccount(accountNumber)
                .switchIfEmpty(Mono.error(new NoSuchAccountException("There is no such account with account number: " + accountNumber)))
                .then(repository.deleteAccount(accountNumber))
                .onErrorMap(DataAccessException.class, e -> new InternalErrorException(e.getMessage()));
    }

    @Override
    public Mono<Void> deleteAccounts(SecurityNumber securityNumber) {
        return repository.fetchAccounts(securityNumber)
                .switchIfEmpty(Flux.error(new NoSuchAccountException("There is no such account with security number: " + securityNumber)))
                .then(repository.deleteAccounts(securityNumber))
                .onErrorMap(DataAccessException.class, e -> new InternalErrorException(e.getMessage()));
    }
}
