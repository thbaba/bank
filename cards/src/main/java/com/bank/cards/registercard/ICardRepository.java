package com.bank.cards.registercard;

import com.bank.cards.common.Card;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ICardRepository extends ReactiveCrudRepository<Card,Integer> {

    @Query("INSERT INTO cards (account_id, card_limit) VALUES (:account_id, :limit) RETURNING *")
    public Mono<Card> register(@Param("account_id") Integer account_id, @Param("limit") Float limit);

}
