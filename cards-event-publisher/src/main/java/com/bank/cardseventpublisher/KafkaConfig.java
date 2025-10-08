package com.bank.cardseventpublisher;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic balanceTopic() {
        return TopicBuilder.name("card-balance-event")
                .replicas(3)
                .partitions(10)
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE)
                .config(TopicConfig.RETENTION_BYTES_CONFIG, "-1")
                .config(TopicConfig.RETENTION_MS_CONFIG, "-1")
                .build();
    }

    @Bean
    public NewTopic cardTopic() {
        return TopicBuilder.name("card-event")
                .replicas(3)
                .partitions(10)
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.COMPRESSION_LZ4_LEVEL_CONFIG)
                .config(TopicConfig.RETENTION_BYTES_CONFIG, "3221225472")
                .config(TopicConfig.RETENTION_MS_CONFIG, "-1")
                .build();
    }

    @Bean
    public ApplicationRunner applicationRunner(PublishService service) {
        return args -> {
            AtomicBoolean lock = new AtomicBoolean(false);

            Flux.interval(Duration.ofSeconds(1))
                    .flatMap(i -> lock.compareAndSet(false, true) ?
                            service.publish().doFinally(_ -> lock.set(false)) : Flux.empty())
                    .onErrorContinue((ex, o) -> System.out.println("Publishing error: " + ex.getMessage()))
                    .subscribe();
        };
    }

    @Bean
    public ProducerFactory<Integer, String> producerFactory(KafkaProperties properties) {
        Map<String, Object> config = properties.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(config, new IntegerSerializer(), new StringSerializer());
    }

    @Bean
    public KafkaTemplate<Integer, String> kafkaTemplate(ProducerFactory<Integer, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

}
