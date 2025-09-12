package com.denizcanbank.cards.application.domain.exception;

public class CardNotFoundException extends RuntimeException {
  public CardNotFoundException(String message) {
    super(message);
  }
}
