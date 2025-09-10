package com.denizcanbank.accounts.application.domain.exception;

public class InvalidAccountInformationException extends RuntimeException {
    public InvalidAccountInformationException(String message) {
        super(message);
    }
}
