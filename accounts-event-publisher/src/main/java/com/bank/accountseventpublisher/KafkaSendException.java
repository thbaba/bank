package com.bank.accountseventpublisher;

public class KafkaSendException extends RuntimeException {
    public KafkaSendException(String message) {
        super(message);
    }

    public KafkaSendException(String message, Exception exception) {
      super(message, exception);
    }
}
