package com.denizcanbank.cards.application.domain.exception;

public class InvalidWithdrawException extends RuntimeException {
  public InvalidWithdrawException(String message) {
    super(message);
  }
}
