package com.bank.cards.registeraccount;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

@Component
@RequiredArgsConstructor
public class ReactiveKafkaListener {

    private final Consumer<Integer, String> kafkaConsumer;

    private final ExecutorService executorService;

    public Flux<ConsumerRecord<Integer, String>> receive() {
        return Flux.create(emitter -> {
            executorService.execute(() -> {
                try {
                    var records = kafkaConsumer.poll(Duration.ofMillis(100));
                    for (var record : records) {
                        emitter.next(record);
                    }
                    emitter.complete();
                } catch (Exception e) {
                    emitter.error(e);
                }
            });
        });
    }

    public Mono<Void> commit() {
        return Mono.create(emitter -> {
            executorService.execute(() -> {
                try {
                    kafkaConsumer.commitSync();
                    emitter.success();
                } catch (Exception e) {
                    emitter.error(e);
                }
            });
        });
    }

    @PreDestroy
    public void close() {
        kafkaConsumer.close();
    }

}
