package com.bank.cardseventpublisher;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IEventRepository{

    Flux<Event> fetch();

    Mono<Void> done(Integer event_id);

}
