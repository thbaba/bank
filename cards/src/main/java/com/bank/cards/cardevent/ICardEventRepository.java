package com.bank.cards.cardevent;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ICardEventRepository extends ReactiveCrudRepository<CardEvent, Integer> {
}
