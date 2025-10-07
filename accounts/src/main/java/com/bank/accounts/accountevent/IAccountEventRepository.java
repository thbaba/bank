package com.bank.accounts.accountevent;

import reactor.core.publisher.Mono;

public interface IAccountEventRepository {

    Mono<Void> register(String topic, Integer key, String payload);

}
