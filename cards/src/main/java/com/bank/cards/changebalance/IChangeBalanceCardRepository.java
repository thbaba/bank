package com.bank.cards.changebalance;

import com.bank.cards.common.Card;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IChangeBalanceCardRepository extends ReactiveCrudRepository<Card, Integer> {

    @Query("UPDATE cards SET balance = :balance WHERE id = :id")
    public Mono<Void> updateBalance(@Param("id") Integer id, @Param("balance") Float balance);

}
