package com.bank.cardseventpublisher;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

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
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE)
                .config(TopicConfig.RETENTION_BYTES_CONFIG, "3221225472")
                .config(TopicConfig.RETENTION_MS_CONFIG, "-1")
                .build();
    }

}
