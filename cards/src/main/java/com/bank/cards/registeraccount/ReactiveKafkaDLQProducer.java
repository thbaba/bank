package com.bank.cards.registeraccount;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class ReactiveKafkaDLQProducer {

    private final Producer<Integer, String> dlqProducer;

    public Mono<Integer> publish(ConsumerRecord<Integer, String> consumerRecord) {
        return Mono.create(sink -> {
            var record = toRecord(consumerRecord);
            dlqProducer.send(record, (metadata, ex) -> {
                if(Objects.nonNull(ex)) {
                    sink.error(new KafkaSendException(String.format("Error on DLQ Record Key: %s", record.key()), ex));
                } else {
                    sink.success(record.key());
                }
            });
        });
    }

    private ProducerRecord<Integer, String> toRecord(ConsumerRecord<Integer, String> consumerRecord) {
        return new ProducerRecord<>(
                "accounts-register-outbox-dlq",
                null,
                null,
                consumerRecord.key(),
                consumerRecord.value(),
                consumerRecord.headers()
        );
    }

    @PreDestroy
    public void close() {
        dlqProducer.close();
    }

}
