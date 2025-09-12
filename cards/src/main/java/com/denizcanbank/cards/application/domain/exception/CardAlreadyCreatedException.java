package com.denizcanbank.cards.application.domain.exception;

public class CardAlreadyCreatedException extends RuntimeException {
    public CardAlreadyCreatedException(String message) {
        super(message);
    }
}
