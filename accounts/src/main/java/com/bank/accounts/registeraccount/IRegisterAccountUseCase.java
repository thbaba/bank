package com.bank.accounts.registeraccount;

import reactor.core.publisher.Mono;

public interface IRegisterAccountUseCase {

    Mono<Account> registerAccount(String securityNumber);

}
