package com.bank.materializedview;

import com.bank.materializedview.cardevent.CardEvent;
import org.bson.Document;

import java.time.ZoneOffset;
import java.util.Date;

public class CardEventToDocumentConverter {

    private final CardEvent event;

    public CardEventToDocumentConverter(CardEvent event) {
        this.event = event;
    }

    public Document document() {
        return new Document()
                .append("card-id", event.getCardId())
                .append("account-id", event.getAccountId())
                .append("card-limit",  event.getLimit())
                .append("card-balance", event.getBalance())
                .append("event-creation-time", Date.from(event.getEventCreationTime().toInstant(ZoneOffset.UTC)));
    }

}
