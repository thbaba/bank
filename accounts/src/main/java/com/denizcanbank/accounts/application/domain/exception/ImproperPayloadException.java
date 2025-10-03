package com.denizcanbank.accounts.application.domain.exception;

import com.denizcanbank.accounts.application.domain.valueObject.Payload;
import lombok.Getter;

public class ImproperPayloadException extends RuntimeException {

    @Getter
    private final Payload payload;

    public ImproperPayloadException(String message) {
        super(message);
        this.payload = null;
    }

    public ImproperPayloadException(String message, Payload payload) {
        super(message);
        this.payload = payload;
    }
}
