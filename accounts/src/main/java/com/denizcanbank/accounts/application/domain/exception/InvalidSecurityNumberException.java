package com.denizcanbank.accounts.application.domain.exception;

public class InvalidSecurityNumberException extends RuntimeException {
  public InvalidSecurityNumberException(String message) {
    super(message);
  }
}
