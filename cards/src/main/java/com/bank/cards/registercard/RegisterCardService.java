package com.bank.cards.registercard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RegisterCardService implements IRegisterCardUseCase{

    private final ICardRepository cardRepository;

    private final IAccountRepository accountRepository;

    private final TransactionalOperator transactionalOperator;
    
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
        );
    }
}
