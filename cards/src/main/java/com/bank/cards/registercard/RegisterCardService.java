package com.bank.cards.registercard;

import com.bank.cards.cardevent.ICardEventRepository;
import com.bank.cards.cardevent.CardUpdateEventPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RegisterCardService implements IRegisterCardUseCase{

    private final ICardRepository cardRepository;

    private final ICardEventRepository cardEventRepository;

    private final IAccountRepository accountRepository;

    private final TransactionalOperator transactionalOperator;

    private final ObjectMapper objectMapper;
    
    @Override
    public Mono<Void> registerCard(String securityNumber, Float limit) {
        return transactionalOperator.transactional(
                accountRepository.fetch(securityNumber)
                        .switchIfEmpty(Mono.error(new NoSuchAccountException(securityNumber)))
                        .flatMap(account -> {
                            account.addLimit(limit);
                            return accountRepository.updateTotalLimit(account.getId(), account.getTotalLimit()).thenReturn(account.getId());
                        })
                        .flatMap(accountId -> cardRepository.register(accountId, limit))
                        .flatMap(card -> {
                            CardUpdateEventPayload payload = new CardUpdateEventPayload(card.getId(), card.getAccountId(), card.getLimit(), card.getBalance());
                            return cardEventRepository.register("CREATE", "card-event", card.getId(), payload.toJson(objectMapper));
                        })
        );
    }
}
