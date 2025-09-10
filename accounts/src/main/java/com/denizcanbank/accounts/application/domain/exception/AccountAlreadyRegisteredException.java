package com.denizcanbank.accounts.application.domain.exception;

public class AccountAlreadyRegisteredException extends RuntimeException {
    public AccountAlreadyRegisteredException(String message) {
        super(message);
    }
}
