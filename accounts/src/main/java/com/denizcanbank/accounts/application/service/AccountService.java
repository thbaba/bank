package com.denizcanbank.accounts.application.service;

import com.denizcanbank.accounts.application.domain.entity.Account;
import com.denizcanbank.accounts.application.domain.exception.AccountAlreadyRegisteredException;
import com.denizcanbank.accounts.application.domain.exception.InternalErrorException;
import com.denizcanbank.accounts.application.domain.exception.NoSuchAccountException;
import com.denizcanbank.accounts.application.domain.repository.IAccountRepository;
import com.denizcanbank.accounts.application.domain.service.AccountComparisonService;
import com.denizcanbank.accounts.application.domain.service.AccountConsistencyService;
import com.denizcanbank.accounts.application.domain.usecase.CrudUseCase;
import com.denizcanbank.accounts.application.domain.valueObject.AccountNumber;
import com.denizcanbank.accounts.application.domain.valueObject.SecurityNumber;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountService implements CrudUseCase {

    private final IAccountRepository repository;

    private final AccountConsistencyService accountConsistencyService;

    private final AccountComparisonService accountComparisonService;

    public AccountService(IAccountRepository repository,
                          AccountConsistencyService accountConsistencyService,
                          AccountComparisonService accountComparisonService) {
        this.repository = repository;
        this.accountConsistencyService = accountConsistencyService;
        this.accountComparisonService = accountComparisonService;
    }

    @Override
    public Mono<Account> registerAccount(Account account) {
        return repository.fetchAccounts(account.securityNumber())
                .filter(fetchedAccount -> accountComparisonService.equalsWithoutID(account, fetchedAccount))
                .flatMap(fetchAccount -> Flux.error(new AccountAlreadyRegisteredException("Account already registered: " + fetchAccount.accountNumber())))
                .switchIfEmpty(repository.registerAccount(account))
                .cast(Account.class)
                .onErrorMap(DataAccessException.class, e -> new InternalErrorException(e.getMessage()))
                .singleOrEmpty();
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
