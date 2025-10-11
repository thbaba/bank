package com.bank.materializedview;

import com.bank.materializedview.cardevent.CardEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CardEventService {


    private final ICardRepository cardRepository;

    /*
    * Do mongo db things...
    * It should be a materialized view
    * Time based compression if an event duplicates it eliminate it
    * Idempotent Consumer !!!
    * TODO It should update Card view in mongodb.
    * */
    public Mono<CardEvent> processCardEvent(CardEvent cardEvent) {

        System.out.println("DENOO");
        System.out.println("CARDEVENT");
        System.out.println(cardEvent);

        return cardRepository.getCard(cardEvent.getCardId())
                .switchIfEmpty(Mono.just(new Card(null, null, null, null, LocalDateTime.now().minusHours(1))))
                .filter(card -> card.getEventCreationTime().isBefore(cardEvent.getEventCreationTime()))
                .flatMap(_ -> cardRepository.updateCard(cardEvent))
                .thenReturn(cardEvent);
    }

}
