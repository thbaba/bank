package com.denizcanbank.cards.application.domain.repository;

import com.denizcanbank.cards.application.domain.entity.Card;
import com.denizcanbank.cards.application.domain.valueObject.ID;
import reactor.core.publisher.Mono;

public interface ICrudRepository {

    Mono<Card> save(Card card);

    Mono<Card> readById(ID card);

    Mono<Card> update(Card card);

    Mono<Void> deleteById(ID card);

}
