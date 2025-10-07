package com.bank.accountseventpublisher;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IAccountEventRepository extends ReactiveCrudRepository<AccountEvent, Integer> {

    @Query("SELECT id, topic, key, payload FROM events WHERE status='PENDING' ORDER BY id limit 1")
    Mono<AccountEvent> fetch();

    @Query("UPDATE events SET status='DONE' WHERE id=:id")
    Mono<Void> done(@Param("id") Integer id);

    @Query("DELETE FROM events WHERE status='DONE'")
    Mono<Void> clean();

}
