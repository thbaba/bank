package com.bank.cardseventpublisher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class PublishService {

    private final EventRepository eventRepository;

    private final ReactiveKafkaTemplate template;

    private final TransactionalOperator transactionalOperator;

    public Flux<Void> publish() {
        return eventRepository.fetch()
                .flatMap(template::publish)
                .flatMap(eventRepository::done)
                .as(transactionalOperator::transactional);
    }

}
