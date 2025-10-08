package com.bank.cardseventpublisher;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class ReactiveKafkaTemplate {

    private final KafkaTemplate<Integer, String> kafkaTemplate;

    public Mono<Integer> publish(Event event) {
        var record = toRecord(event);
        return Mono.fromFuture(kafkaTemplate.send(record)).thenReturn(event.getId());
    }

    private ProducerRecord<Integer, String> toRecord(Event event) {
        ProducerRecord<Integer, String> record = new ProducerRecord<>(event.getTopic(), event.getKey(), event.getPayload());

        record.headers().add("event-id", ByteBuffer.allocate(4).putInt(event.getId()).array())
                .add("event-name", event.getName().getBytes(StandardCharsets.UTF_8))
                .add("creation-time", event.getCreated_at().toString().getBytes(StandardCharsets.UTF_8));

        return record;
    }

}
