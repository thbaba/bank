package com.denizcanbank.cards.application.service;

import com.denizcanbank.cards.application.domain.entity.Card;
import com.denizcanbank.cards.application.domain.repository.ICardAccountIDRepository;
import com.denizcanbank.cards.application.domain.usecase.ReadCardByAccountIDUseCase;
import com.denizcanbank.cards.application.domain.valueObject.ID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CardByAccountIDService implements ReadCardByAccountIDUseCase {

    private final ICardAccountIDRepository repository;

    @Override
    public Flux<Card> readByAccountID(String accountID) {
        return Mono.just(ID.of(accountID))
                .flatMapMany(repository::readCardsByAccountID);
    }
}
