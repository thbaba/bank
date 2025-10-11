package com.bank.materializedview.cardevent;

import lombok.*;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class CardEvent {
    private final Integer eventId;
    private final String eventName;
    private final LocalDateTime eventCreationTime;

    private final Integer cardId;
    private final Integer accountId;
    private final Float limit;
    private final Float balance;

    @Getter(AccessLevel.NONE)
    private final TopicPartition topicPartition;

    @Getter(AccessLevel.NONE)
    private final OffsetAndMetadata offsetAndMetadata;

    @Getter(AccessLevel.NONE)
    private IVoidFunction ackFunc;

    public static Mono<Void> ack(CardEvent event) {
        event.ackFunc.apply();
        return Mono.empty();
    }
}
