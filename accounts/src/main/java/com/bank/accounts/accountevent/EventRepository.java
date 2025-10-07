package com.bank.accounts.accountevent;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class EventRepository implements IAccountEventRepository {

    private final DatabaseClient dbClient;

    @Override
    public Mono<Void> register(String topic, Integer key, String payload) {
        String query = "INSERT INTO events (topic, key, payload) VALUES (:topic, :key, :payload)";

        return dbClient.sql(query)
                .bind("topic", topic)
                .bind("key", key)
                .bind("payload", payload)
                .then();
    }
}
