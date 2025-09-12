package com.denizcanbank.cards.application.service;

import com.denizcanbank.cards.application.domain.entity.Card;
import com.denizcanbank.cards.application.domain.exception.CardAlreadyCreatedException;
import com.denizcanbank.cards.application.domain.exception.CardNotFoundException;
import com.denizcanbank.cards.application.domain.repository.ICrudRepository;
import com.denizcanbank.cards.application.domain.usecase.CardCrudUseCase;
import com.denizcanbank.cards.application.domain.valueObject.ID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CardService implements CardCrudUseCase {

    private final ICrudRepository repository;

    public CardService(ICrudRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Card> create(Card card) {
        return Mono.just(card)
                .cast(Card.class)
                .flatMap(repository::save);
    }

    @Override
    public Mono<Card> read(String id) {
        return Mono.just(ID.of(id))
                .flatMap(repository::readById)
                .switchIfEmpty(Mono.error(new CardNotFoundException("Card not found")));
    }

    @Override
    public Mono<Card> update(Card card) {
        return read(card.cardID().toString())
                .filter(readCard -> readCard.cardID().equals(card.cardID()))
                .switchIfEmpty(Mono.error(new CardNotFoundException("No such a card")))
                .then(Mono.just(card))
                .flatMap(repository::update);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(ID.of(id));
    }
}
