package com.bank.cards.registeraccount;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DLQEventWriter implements ApplicationContextAware {

    private final ReactiveKafkaDLQProducer reactiveKafkaProducer;

    private ApplicationContext applicationContext;

    public void write(ConsumerRecord<Integer, String> record) {
        reactiveKafkaProducer
                .publish(record)
                .doOnError(ex -> {
                    System.out.println("Account event DLQ publish failed: " + ex.getMessage());
                    SpringApplication.exit(applicationContext, () -> -10);
                    System.exit(-10);
                })
                .subscribe();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
