package com.denizcanbank.cards.application.domain.usecase;

import com.denizcanbank.cards.application.domain.entity.Card;
import reactor.core.publisher.Flux;

public interface ReadCardByAccountIDUseCase {

    Flux<Card> readByAccountID(String accountID);

}
