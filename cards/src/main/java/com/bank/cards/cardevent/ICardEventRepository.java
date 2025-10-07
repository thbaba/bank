package com.bank.cards.cardevent;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ICardEventRepository extends ReactiveCrudRepository<CardEvent, Integer> {

    @Query("INSERT INTO events (name, topic, key, payload) VALUES (:name, :topic, :key, :payload)")
    public Mono<Void> register(@Param("name")  String name, @Param("topic") String topic, @Param("key") Integer key, @Param("payload") String payload);

}
