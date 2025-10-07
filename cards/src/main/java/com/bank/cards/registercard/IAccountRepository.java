package com.bank.cards.registercard;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IAccountRepository extends ReactiveCrudRepository<Account,Integer> {

    @Query("SELECT id, total_limit FROM accounts WHERE security_number = :securityNumber")
    Mono<Account> fetch(@Param("securityNumber") String securityNumber);

    @Query("UPDATE accounts SET total_limit = :totalLimit WHERE id = :id")
    Mono<Boolean> updateTotalLimit(@Param("id") Integer id, @Param("totalLimit") Float totalLimit);

}
