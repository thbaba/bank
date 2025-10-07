package com.bank.accounts.registeraccount;

import reactor.core.publisher.Mono;

public interface IRegisterAccountRepository {

    Mono<Integer> registerAccount(String securityNumber);

    Mono<Boolean> isAccountExists(String securityNumber);

}
