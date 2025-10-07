package com.bank.cards.registeraccount;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    public NewTopic dlqTopic() {
        Map<String, String> config  = new HashMap<>();
        config.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE);
        config.put(TopicConfig.RETENTION_BYTES_CONFIG, "3221225472");
        config.put(TopicConfig.RETENTION_MS_CONFIG, "-1");
        config.put(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "3");
        return new NewTopic("accounts-register-outbox-dlq",         1, (short) 3).configs(config);
    }

    @Bean
    public Producer<Integer, String> dlqProducer() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9001,localhost:9002,localhost:9003");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, Integer.MAX_VALUE);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        config.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 100);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return new KafkaProducer<Integer, String>(config);
    }

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

    @Bean
    public ApplicationRunner createTopicApplicationRunner(EventReader eventReader, DLQEventWriter eventWriter) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                createTopic(dlqTopic());

                AtomicBoolean semaphore = new AtomicBoolean(false);

                Flux.interval(Duration.ofSeconds(1))
                        .flatMap(i -> semaphore.compareAndSet(false, true) ?
                                    eventReader.read().doFinally(_ -> semaphore.set(false)) : Flux.empty())
                        .onErrorContinue(NewAccountEventProcessingException.class, (ex, o) -> {
                            System.out.println("Account Processing Failed: " + ex.getMessage());
                            eventWriter.write(((NewAccountEventProcessingException)ex).getRecord());
                        })
                        .onErrorContinue(Exception.class, (ex, o) -> System.out.println("Account Processing Failed: " + ex.getMessage()))
                        .subscribe();
            }

            private void createTopic(NewTopic topic) {
                Mono.just(topic)
                        .flatMap(newTopic -> {
                            try(AdminClient client = adminClientFactory().get()) {
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
            }
        };
    }

}
