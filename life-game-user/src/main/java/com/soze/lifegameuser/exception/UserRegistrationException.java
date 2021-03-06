package com.soze.lifegameuser.exception;

public class UserRegistrationException extends RuntimeException {

  private final String field;

  public UserRegistrationException(String field, String message) {
    super(message);
    this.field = field;
  }

  public String getField() {
    return field;
  }
}
