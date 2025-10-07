package com.bank.accountseventpublisher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AccountEventPublisher {

    private final ReactiveKafkaPublisher kafkaPublisher;

    private final IAccountEventRepository eventRepository;

    public Mono<Void> publish() {
        return eventRepository.fetch()
                .flatMap(kafkaPublisher::publish)
                .flatMap(this::markDone);
    }

    private Mono<Void> markDone(Integer id) {
        return eventRepository.done(id);
    }

}
