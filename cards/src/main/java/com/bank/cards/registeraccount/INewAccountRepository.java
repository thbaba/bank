package com.bank.cards.registeraccount;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface INewAccountRepository extends ReactiveCrudRepository<NewAccount,Integer> {

    @Query("SELECT EXISTS(SELECT 1 FROM accounts WHERE id = :id)")
    Mono<Boolean> exists(@Param("id") Integer id);

    @Query("INSERT INTO accounts (id, security_number) VALUES (:id, :security_number)")
    Mono<Void> register(@Param("id") Integer id, @Param("security_number") String securityNumber);

}
