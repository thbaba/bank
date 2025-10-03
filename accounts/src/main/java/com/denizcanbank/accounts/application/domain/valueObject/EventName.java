package com.denizcanbank.accounts.application.domain.valueObject;


public enum EventName {
    REGISTRATION("AccountRegistrationEvent");

    private final String label;

    private EventName(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
