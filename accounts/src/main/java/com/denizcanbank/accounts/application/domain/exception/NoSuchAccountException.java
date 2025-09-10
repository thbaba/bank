package com.denizcanbank.accounts.application.domain.exception;

public class NoSuchAccountException extends RuntimeException {
    public NoSuchAccountException(String message) {
        super(message);
    }
}
