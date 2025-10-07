package com.bank.cards.registeraccount;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class NewAccountEventProcessingException extends RuntimeException {

    private final ConsumerRecord<Integer, String> record;

    public NewAccountEventProcessingException(String message, ConsumerRecord<Integer, String> record) {
        this.record = record;
        super(message);
    }

    public ConsumerRecord<Integer, String> getRecord() {
        return record;
    }
}
