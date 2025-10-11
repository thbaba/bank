package com.bank.materializedview;

import com.bank.materializedview.cardevent.CardEvent;
import reactor.core.publisher.Mono;

import javax.smartcardio.Card;

public interface ICardRepository {

    Mono<Void> updateCard(CardEvent cardEvent);

    Mono<Card> getCard(Integer id);

}
