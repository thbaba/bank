package com.denizcanbank.cards.repository;

import com.denizcanbank.cards.application.domain.entity.Card;
import com.denizcanbank.cards.application.domain.repository.ICrudRepository;
import com.denizcanbank.cards.application.domain.valueObject.ID;
import com.denizcanbank.cards.application.domain.valueObject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CardRepository implements ICrudRepository {

    private final DatabaseClient client;

    @Override
    public Mono<Card> save(Card card) {
        return client.sql("insert into cards (account_id, total_limit, amount_used) " +
                "values (:accountID, :totalLimit, :amountUsed) returning *")
                .bindValues(Map.ofEntries(
                        Map.entry("accountID", card.accountID().asUUID()),
                        Map.entry("totalLimit", card.totalLimit().amount()),
                        Map.entry("amountUsed", card.amountUsed().amount())
                ))
                .map((row, meta) -> Card.builder()
                        .cardID(ID.of(row.get("card_id", UUID.class)))
                        .accountID(ID.of(row.get("account_id", UUID.class)))
                        .totalLimit(Money.of(row.get("total_limit", Float.class)))
                        .amountUsed(Money.of(row.get("amount_used", Float.class)))
                        .build())
                .one();
    }

    @Override
    public Mono<Card> readById(ID id) {
        return client.sql("select * from cards where card_id = :cardID")
                .bind("cardID", id.asUUID())
                .map((row, meta) -> Card.builder()
                        .cardID(ID.of(row.get("card_id", UUID.class)))
                        .accountID(ID.of(row.get("account_id", UUID.class)))
                        .totalLimit(Money.of(row.get("total_limit", Float.class)))
                        .amountUsed(Money.of(row.get("amount_used", Float.class)))
                        .build())
                .one();
    }

    @Override
    public Mono<Card> update(Card card) {
        return client.sql("update cards set total_limit = :totalLimit, amount_used = :amountUsed where card_id = :cardID returning *")
                .bind("cardID", card.cardID().asUUID())
                .bind("totalLimit", card.totalLimit().amount())
                .bind("amountUsed", card.amountUsed().amount())
                .map((row, meta) -> Card.builder()
                        .cardID(ID.of(row.get("card_id", UUID.class)))
                        .accountID(ID.of(row.get("account_id", UUID.class)))
                        .totalLimit(Money.of(row.get("total_limit", Float.class)))
                        .amountUsed(Money.of(row.get("amount_used", Float.class)))
                        .build())
                .one();
    }

    @Override
    public Mono<Void> deleteById(ID card) {
        return client.sql("delete from cards where card_id = :cardID")
                .bind("cardID", card.asUUID())
                .then();
    }
}
