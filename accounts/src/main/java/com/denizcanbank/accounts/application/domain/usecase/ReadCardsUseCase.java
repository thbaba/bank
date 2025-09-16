package com.denizcanbank.accounts.application.domain.usecase;

import com.denizcanbank.accounts.application.domain.entity.Card;
import reactor.core.publisher.Flux;

public interface ReadCardsUseCase {

    Flux<Card>  readCardsByAccountID(String accountID);

}
