package com.bank.materializedview;

import com.bank.materializedview.cardevent.CardEvent;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Repository
@RequiredArgsConstructor
public class CardRepository implements ICardRepository {

    private final MongoClient mongo;

    @Override
    public Mono<Void> updateCard(CardEvent cardEvent) {
        MongoDatabase db = mongo.getDatabase("materialized-view");
        MongoCollection<Document> collection = db.getCollection("cards");

        return Mono.from(collection.replaceOne(
                Filters.eq("card-id", cardEvent.getCardId()),
                new CardEventToDocumentConverter(cardEvent).document(),
                new ReplaceOptions().upsert(true)
                )).then();
    }

    @Override
    public Mono<Card> getCard(Integer id) {
        MongoDatabase db = mongo.getDatabase("materialized-view");
        MongoCollection<Document> collection = db.getCollection("cards");

        return Mono.from(collection.find(Filters.eq("card-id", id)))
                .map(doc -> {
                    Integer cardId = doc.getInteger("card-id");
                    Integer accountId = doc.getInteger("account-id");
                    Float limit = doc.getDouble("card-limit").floatValue();
                    Float balance = doc.getDouble("card-balance").floatValue();
                    LocalDateTime eventCreationTime = LocalDateTime.ofInstant(doc.getDate("event-creation-time").toInstant(), ZoneOffset.UTC);

                    return new Card(cardId, accountId, limit, balance, eventCreationTime);
                });
    }
}
