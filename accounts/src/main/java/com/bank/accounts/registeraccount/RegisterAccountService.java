package com.bank.accounts.registeraccount;

import com.bank.accounts.accountevent.IAccountEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class RegisterAccountService implements IRegisterAccountUseCase{

    private final IRegisterAccountRepository registerAccountRepository;

    private final IAccountEventRepository accountEventRepository;

    private final TransactionalOperator transactionalOperator;

    private final AccountPayloadJsonSerializer accountPayloadJsonSerializer;

    @Override
    public Mono<Account> registerAccount(String securityNumber) {
        return transactionalOperator.transactional(
                registerAccountRepository.isAccountExists(securityNumber)
                        .flatMap(exists -> registerAccount(securityNumber, exists))
                        .flatMap(account -> registerEvent(account.getSecurityNumber(), account.getId()).thenReturn(account))
        );
    }

    private Mono<Account> registerAccount(String securityNumber, Boolean exists) {
        if (exists) {
            return Mono.error(new AccountAlreadyRegisteredException("Account already exists!"));
        } else {
            return registerAccountRepository.registerAccount(securityNumber)
                    .map(id -> new Account(id, securityNumber));
        }
    }

    private Mono<Void> registerEvent(String securityNumber, Integer id) {
        String topic = "accounts-register-outbox";
        return Mono.just(new AccountPayload(securityNumber, id))
                .publishOn(Schedulers.parallel())
                .map(payload -> payload.toJson(accountPayloadJsonSerializer))
                .publishOn(Schedulers.immediate())
                .flatMap(payload -> accountEventRepository.register(topic, id, payload));
    }
}
