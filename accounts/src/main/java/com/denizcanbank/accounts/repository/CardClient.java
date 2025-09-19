package com.denizcanbank.accounts.repository;

import com.denizcanbank.accounts.application.domain.entity.Card;
import com.denizcanbank.accounts.application.domain.repository.ICardClient;
import com.denizcanbank.accounts.application.domain.valueObject.ID;
import com.denizcanbank.accounts.dto.CardClientDto;
import com.denizcanbank.accounts.mapper.CardClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class CardClient implements ICardClient {

    private final WebClient cardWebClient;

    private final CardClientMapper cardMapper;

    private final ReactiveCircuitBreaker cardCircuitBreaker;

    @Override
    public Flux<Card> accountsCards(ID accountID) {
        String uri = String.format("/api/account/%s", accountID.toString());

        return cardWebClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(CardClientDto.class)
                .map(cardMapper::toEntity)
                .transformDeferred(f -> cardCircuitBreaker.run(f, throwable -> {
                    System.out.println("Error: " + throwable.getMessage());
                    return Flux.empty();
                }));
    }
}
