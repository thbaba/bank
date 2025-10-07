package com.bank.accounts.registeraccount;

public class AccountAlreadyRegisteredException extends RuntimeException {
    public AccountAlreadyRegisteredException(String message) {
        super(message);
    }
}
