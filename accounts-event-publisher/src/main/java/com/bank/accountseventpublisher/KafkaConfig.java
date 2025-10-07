package com.bank.accountseventpublisher;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@Configuration
public class KafkaConfig {

    @Bean
    public Supplier<AdminClient> adminClientFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9001,localhost:9002,localhost:9003");
        return () -> AdminClient.create(config);
    }

    @Bean
    public NewTopic newTopic() {
        Map<String, String> config  = new HashMap<>();
        config.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE);
        config.put(TopicConfig.RETENTION_BYTES_CONFIG, "3221225472");
        config.put(TopicConfig.RETENTION_MS_CONFIG, "-1");
        config.put(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "3");
        return new NewTopic("accounts-register-outbox",         10, (short) 3).configs(config);
    }

    @Bean
    public Producer<Integer, String> kafkaProducer() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9001,localhost:9002,localhost:9003");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        config.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 100);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return new KafkaProducer<Integer, String>(config);
    }

    @Bean
    public ApplicationRunner createTopicApplicationRunner(Supplier<AdminClient> adminFactory, NewTopic newTopic, AccountEventPublisher eventPublisher) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                Mono.just(newTopic)
                        .flatMap(newTopic -> {
                            try(AdminClient client = adminFactory.get()) {
                                client.createTopics(List.of(newTopic)).all().get();
                                return Mono.empty();
                            } catch (InterruptedException | ExecutionException e) {
                                return Mono.error(e);
                            }
                        })
                        .onErrorResume(ExecutionException.class, ex -> {
                            System.out.println(ex.getMessage());
                            return Mono.empty();
                        })
                        .subscribe();

                AtomicBoolean semaphore = new AtomicBoolean(false);

                Flux.interval(Duration.ofSeconds(1))
                        .flatMap(i -> semaphore.compareAndSet(false, true) ?
                                    eventPublisher.publish().doFinally(_ -> semaphore.set(false)) : Flux.empty())
                        .onErrorContinue(Exception.class, (ex, o) -> System.out.println("Publishing Failed: " + ex.getMessage()))
                        .subscribe();
            }
        };
    }

}
