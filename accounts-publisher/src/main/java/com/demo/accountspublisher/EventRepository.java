package com.demo.accountspublisher;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface EventRepository extends ReactiveCrudRepository<Event, UUID> {

    @Query("SELECT event_id, event_name, aggregate_id, payload FROM events WHERE status='PENDING'")
    Flux<Event> find();

    @Query("UPDATE events SET status='DONE' WHERE event_id = :id")
    Mono<Void> setProcessed(@Param("id") UUID id);

}
