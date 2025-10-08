package com.bank.cardseventpublisher;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class EventRepository implements IEventRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<Event> fetch() {
        String query = "SELECT id, name, topic, key, payload, status, created_at FROM events WHERE status='PENDING' ORDER BY id LIMIT 100 FOR UPDATE";

        return databaseClient.sql(query)
                .map(this::rowMap)
                .all();
    }

    @Override
    public Mono<Void> done(Integer event_id) {
        String query = "UPDATE events SET status='DONE' WHERE id=:d";

        return databaseClient.sql(query).bind(0, event_id).then();
    }

    private Event rowMap(Row row, RowMetadata rowMetadata) {
        return new Event(
                row.get(0, Integer.class),
                row.get(1, String.class),
                row.get(2, String.class),
                row.get(3, Integer.class),
                row.get(4, String.class),
                row.get(5, String.class),
                row.get(6, LocalDateTime.class)
        );
    }
}
