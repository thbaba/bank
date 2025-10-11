package com.bank.materializedview;

import com.bank.materializedview.cardevent.CardEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CardEventService {

    /*
    * Do mongo db things...
    * It should be a materialized view
    * TODO It should update Card view in mongodb.
    * */
    public Mono<CardEvent> processCardEvent(CardEvent cardEvent) {

        System.out.println("DENOO");
        System.out.println("CARDEVENT");
        System.out.println(cardEvent);

        return Mono.just(cardEvent);
    }

}
