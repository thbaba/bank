package com.bank.materializedview.cardevent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Executor;

@Component
@RequiredArgsConstructor
public class ReactiveKafkaListener {

    private final Consumer<Integer, String> consumer;

    private final Sinks.Many<CardEvent> emitter;

    private final Executor executor;

    private final ObjectMapper objectMapper;

    private final Map<TopicPartition, OffsetAndMetadata> offsets;

    private boolean started = false;

    public Flux<CardEvent> get() {
        if(!started) {
            consumer.subscribe(Collections.singleton("card-event"));
            executor.execute(this::poll);
            started = true;
        }

        return emitter.asFlux();
    }

    private void poll() {
        while (true) {
            try {

                synchronized (offsets) {
                    if (!offsets.isEmpty()) {
                        consumer.commitSync(offsets);
                        offsets.clear();
                    }
                }

                var records = consumer.poll(Duration.ofSeconds(1));

                records.forEach(record -> {
                    TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                    OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(record.offset() + 1);
                    IVoidFunction ackFunc = () -> executor.execute(() -> {
                        synchronized (offsets) {
                            offsets.put(topicPartition, offsetAndMetadata);
                        }
                    });

                    Integer eventId = ByteBuffer.wrap(record.headers().lastHeader("event-id").value()).getInt();
                    String eventName = new String(record.headers().lastHeader("event-name").value(), StandardCharsets.UTF_8);
                    LocalDateTime eventCreationTime = LocalDateTime.parse(new String(record.headers().lastHeader("creation-time").value(), StandardCharsets.UTF_8));
                    CardEventPayload payload;
                    try {
                        payload = objectMapper.readValue(record.value(), CardEventPayload.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }

                    Integer cardId = payload.id();
                    Integer accountId = payload.account_id();
                    Float limit = payload.limit();
                    Float balance = payload.balance();

                    CardEvent event = CardEvent.builder()
                            .eventId(eventId)
                            .eventName(eventName)
                            .eventCreationTime(eventCreationTime)
                            .cardId(cardId)
                            .accountId(accountId)
                            .limit(limit)
                            .balance(balance)
                            .ackFunc(ackFunc)
                            .build();

                    emitter.tryEmitNext(event);
                });
            } catch (Exception e) {
                emitter.tryEmitError(e);
            }
        }
    }

}
