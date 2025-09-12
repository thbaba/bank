package com.denizcanbank.cards.application.domain.usecase;

import com.denizcanbank.cards.application.domain.entity.Card;
import reactor.core.publisher.Mono;

public interface CardCrudUseCase {

    Mono<Card> create(Card card);

    Mono<Card> read(String id);

    Mono<Card> update(Card card);

    Mono<Void> delete(String id);
}
