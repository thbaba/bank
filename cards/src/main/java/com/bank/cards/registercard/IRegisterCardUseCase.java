package com.bank.cards.registercard;

import reactor.core.publisher.Mono;

public interface IRegisterCardUseCase {

    Mono<Void> registerCard(String securityNumber, Float limit);

}
