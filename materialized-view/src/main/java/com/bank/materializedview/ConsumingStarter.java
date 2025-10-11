package com.bank.materializedview;

import com.bank.materializedview.cardevent.CardEvent;
import com.bank.materializedview.cardevent.ReactiveKafkaListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class ConsumingStarter implements ApplicationRunner {

    private final ReactiveKafkaListener reactiveKafkaListener;

    private final CardEventService cardEventService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Flux<CardEvent> events = reactiveKafkaListener.get();
        events
                .flatMap(cardEventService::processCardEvent)
                .map(CardEvent::ack)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }
}
