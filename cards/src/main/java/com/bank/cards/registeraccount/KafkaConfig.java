package com.bank.cards.registeraccount;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
public class KafkaConfig {

    @Bean
    public ExecutorService executorService() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    public Consumer<Integer, String> kafkaConsumer() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9001,localhost:9002,localhost:9003");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 5);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "card-newAccountConsumer");
        KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(config);
        consumer.subscribe(List.of("accounts-register-outbox"));
        return consumer;
    }

    /*
    * There should be a DLQ to register unprocessed events .
    * */

    @Bean
    public ApplicationRunner createTopicApplicationRunner(EventReader eventReader) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                AtomicBoolean semaphore = new AtomicBoolean(false);

                Flux.interval(Duration.ofSeconds(1))
                        .flatMap(i -> semaphore.compareAndSet(false, true) ?
                                    eventReader.read().doFinally(_ -> semaphore.set(false)) : Flux.empty())
                        .onErrorContinue(NewAccountEventProcessingException.class, (ex, o) -> System.out.println("Account Processing Failed: " + ex.getMessage()))
                        .onErrorContinue(Exception.class, (ex, o) -> System.out.println("Account Processing Failed: " + ex.getMessage()))
                        .subscribe();
            }
        };
    }

}
