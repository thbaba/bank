package com.denizcanbank.accounts.application.domain.repository;

import com.denizcanbank.accounts.application.domain.entity.Card;
import com.denizcanbank.accounts.application.domain.valueObject.ID;
import reactor.core.publisher.Flux;

public interface ICardClient {

    Flux<Card> accountsCards(ID accountID);

}
