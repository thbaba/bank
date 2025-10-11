package com.bank.materializedview.cardevent;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class CardEventConfig {

    @Bean
    public Consumer<Integer, String> cardEventConsumer(ConsumerFactory<Integer,String> factory) {
        return factory.createConsumer();
    }

    @Bean
    public Sinks.Many<CardEvent> emitter() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    @Bean
    public Executor executor() {
        return Executors.newFixedThreadPool(2);
    }

    @Bean
    public Map<TopicPartition, OffsetAndMetadata> offsets() {
        return new HashMap<>();
    }

}
