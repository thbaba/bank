package com.denizcanbank.accounts.application.service;

import com.denizcanbank.accounts.application.domain.entity.Card;
import com.denizcanbank.accounts.application.domain.repository.ICardClient;
import com.denizcanbank.accounts.application.domain.usecase.ReadCardsUseCase;
import com.denizcanbank.accounts.application.domain.valueObject.ID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CardService implements ReadCardsUseCase {

    private final ICardClient cardClient;

    @Override
    public Flux<Card> readCardsByAccountID(String accountID) {
        return cardClient.accountsCards(ID.of(accountID));
    }
}
