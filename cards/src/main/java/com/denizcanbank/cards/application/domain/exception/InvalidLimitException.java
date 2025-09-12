package com.denizcanbank.cards.application.domain.exception;

public class InvalidLimitException extends RuntimeException {
    public InvalidLimitException(String message) {
        super(message);
    }
}
