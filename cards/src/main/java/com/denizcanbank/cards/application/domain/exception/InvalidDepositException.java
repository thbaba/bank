package com.denizcanbank.cards.application.domain.exception;

public class InvalidDepositException extends RuntimeException {
    public InvalidDepositException(String message) {
        super(message);
    }
}
