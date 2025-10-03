package com.demo.accountspublisher;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final KafkaSender<UUID, String> kafkaSender;

    private final EventRepository eventRepository;

    private Flux<SenderResult<UUID>> publish() {
        return kafkaSender.send(eventRepository.find().map(this::toRecord))
                .flatMap(this::setStatusDone)
                .doOnError(ex -> System.out.println("Publishing Failed: " + ex.getMessage()))
                .doOnNext(result -> System.out.println("Event Published: " + result));
    }

    private Mono<SenderResult<UUID>>setStatusDone(SenderResult<UUID> result) {
        return eventRepository.setProcessed(result.correlationMetadata()).thenReturn(result);
    }

    private SenderRecord<UUID, String, UUID> toRecord(Event event) {
        ProducerRecord<UUID, String> producerRecord = new ProducerRecord<>(
            "account",
                event.getAggregateId(),
                event.getPayload()
        );

        return SenderRecord.create(producerRecord, event.getId());
    }

    @PostConstruct
    public void start(){
        Flux.interval(Duration.ofSeconds(3))
                .flatMap(i -> publish())
                .onErrorContinue(Exception.class, (ex, o) -> System.out.println("Publishing Failed: " + ex.getMessage()))
                .subscribe();
    }

    @PreDestroy
    public void clean(){
        kafkaSender.close();
    }

}
