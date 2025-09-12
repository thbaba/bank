package com.denizcanbank.cards.application.domain.exception;

public class NegativeAmountException extends RuntimeException {
  public NegativeAmountException(String message) {
    super(message);
  }
}
