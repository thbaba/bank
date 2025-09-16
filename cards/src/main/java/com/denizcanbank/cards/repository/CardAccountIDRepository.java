package com.denizcanbank.cards.repository;

import com.denizcanbank.cards.application.domain.entity.Card;
import com.denizcanbank.cards.application.domain.repository.ICardAccountIDRepository;
import com.denizcanbank.cards.application.domain.valueObject.ID;
import com.denizcanbank.cards.application.domain.valueObject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CardAccountIDRepository implements ICardAccountIDRepository {

    private final DatabaseClient client;

    @Override
    public Flux<Card> readCardsByAccountID(ID accountID) {
        System.out.println("READ CARD BY ACCOUNT ID" + accountID);
        return client.sql("select * from cards where account_id = :accountID")
                .bind("accountID", accountID.asUUID())
                .map((row, meta) -> Card.builder()
                        .cardID(ID.of(row.get("card_id", UUID.class)))
                        .accountID(ID.of(row.get("account_id", UUID.class)))
                        .totalLimit(Money.of(row.get("total_limit", Float.class)))
                        .amountUsed(Money.of(row.get("amount_used", Float.class)))
                        .build())
                .all();
    }
}
