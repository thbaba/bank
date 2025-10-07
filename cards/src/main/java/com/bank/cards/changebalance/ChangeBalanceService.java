package com.bank.cards.changebalance;

import com.bank.cards.cardevent.CardUpdateEventPayload;
import com.bank.cards.cardevent.ICardEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChangeBalanceService {

    private final IChangeBalanceCardRepository depositCardRepository;

    private final ICardEventRepository cardEventRepository;

    private final ObjectMapper objectMapper;

    private final TransactionalOperator transactionalOperator;

    public Mono<Void> deposit(ChangeBalanceRequestDto dto) {

        Integer id = dto.cardId();
        Float amount = dto.amount();

        return depositCardRepository.findById(id)
                .doOnNext(card -> card.deposit(amount))
                .flatMap(card -> depositCardRepository.updateBalance(card.getId(), card.getBalance()).thenReturn(card))
                .flatMap(card -> {
                    ChangeBalancePayload payload = new ChangeBalancePayload(card.getAccountId(), amount);
                    return cardEventRepository.register("DEPOSIT", "card-balance-event", card.getAccountId(), payload.toJson(objectMapper)).thenReturn(card);
                })
                .flatMap(card -> {
                    CardUpdateEventPayload payload = new CardUpdateEventPayload(card.getId(), card.getAccountId(), card.getLimit(), card.getBalance());
                    return cardEventRepository.register("UPDATE", "card-event", card.getId(), payload.toJson(objectMapper));
                })
                .as(transactionalOperator::transactional);
    }

    public Mono<Void> withdraw(ChangeBalanceRequestDto dto) {
        Integer id = dto.cardId();
        Float amount = dto.amount();

        return depositCardRepository.findById(id)
                .doOnNext(card -> card.withdraw(amount))
                .flatMap(card -> depositCardRepository.updateBalance(card.getId(), card.getBalance()).thenReturn(card))
                .flatMap(card -> {
                    ChangeBalancePayload payload = new ChangeBalancePayload(card.getAccountId(), amount);
                    return cardEventRepository.register("WITHDRAW", "card-balance-event", card.getAccountId(), payload.toJson(objectMapper)).thenReturn(card);
                })
                .flatMap(card -> {
                    CardUpdateEventPayload payload = new CardUpdateEventPayload(card.getId(), card.getAccountId(), card.getLimit(), card.getBalance());
                    return cardEventRepository.register("UPDATE", "card-event", card.getId(), payload.toJson(objectMapper));
                })
                .as(transactionalOperator::transactional);
    }

}
