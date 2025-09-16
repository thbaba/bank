package com.denizcanbank.cards.application.domain.repository;

import com.denizcanbank.cards.application.domain.entity.Card;
import com.denizcanbank.cards.application.domain.valueObject.ID;
import reactor.core.publisher.Flux;

public interface ICardAccountIDRepository {

    Flux<Card> readCardsByAccountID(ID accountID);

}
