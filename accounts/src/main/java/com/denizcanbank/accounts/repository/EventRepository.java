package com.denizcanbank.accounts.repository;

import com.denizcanbank.accounts.application.domain.entity.AccountEvent;
import com.denizcanbank.accounts.application.domain.repository.IEventRepository;
import com.denizcanbank.accounts.application.domain.repository.IPayloadJsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class EventRepository implements IEventRepository {

    private final DatabaseClient client ;

    private final IPayloadJsonConverter converter;

    @Override
    public Mono<Void> saveAccountRegistrationEvent(AccountEvent event) {
        String sqlQuery = "INSERT INTO events (event_name, aggregate_id, payload) VALUES(:eventName, :aggregateId, :payload)";

        return client.sql(sqlQuery)
                .bind("eventName", event.getName().label())
                .bind("aggregateId", event.getAggregateId().asUUID())
                .bind("payload", event.getPayload().convertToJson(converter))
                .then();
    }
}
