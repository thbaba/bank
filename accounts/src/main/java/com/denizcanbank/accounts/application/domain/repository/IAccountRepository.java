package com.denizcanbank.accounts.application.domain.repository;

import com.denizcanbank.accounts.application.domain.entity.Account;
import com.denizcanbank.accounts.application.domain.valueObject.AccountNumber;
import com.denizcanbank.accounts.application.domain.valueObject.SecurityNumber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountRepository {

    Mono<Account> registerAccount(Account account);
    Mono<Account> updateAccount(Account account);
    Mono<Account> fetchAccount(AccountNumber accountNumber);
    Flux<Account> fetchAccounts(SecurityNumber securityNumber);
    Mono<Void> deleteAccount(AccountNumber accountNumber);
    Mono<Void> deleteAccounts(SecurityNumber securityNumber);

}
