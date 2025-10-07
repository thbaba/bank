package com.bank.accountseventpublisher;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReactiveKafkaPublisher {

    private final Producer<Integer, String> kafkaProducer;

    public Mono<Integer> publish(AccountEvent event) {
        return Mono.create(sink -> {
            var record = toRecord(event);
            kafkaProducer.send(record, (metadata, ex) -> {
                if(Objects.nonNull(ex)) {
                    sink.error(new KafkaSendException(String.format("Error on publishing event %s", event.getId()), ex));
                } else {
                    sink.success(event.getId());
                }
            });
        });
    }

    private ProducerRecord<Integer, String> toRecord(AccountEvent event) {
        var record = new ProducerRecord<>(
                event.getTopic(),
                event.getKey(),
                event.getPayload()
        );

        record.headers().add("event-id", ByteBuffer.allocate(4).putInt(event.getId()).array());

        return record;
    }

    @PreDestroy
    public void close() {
        kafkaProducer.close();
    }

}
