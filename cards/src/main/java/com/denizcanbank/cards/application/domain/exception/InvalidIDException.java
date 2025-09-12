package com.denizcanbank.cards.application.domain.exception;

public class InvalidIDException extends RuntimeException {
  public InvalidIDException(String message) {
    super(message);
  }
}
