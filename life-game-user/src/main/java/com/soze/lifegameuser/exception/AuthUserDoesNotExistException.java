package com.soze.lifegameuser.exception;

public class AuthUserDoesNotExistException extends RuntimeException {

  public AuthUserDoesNotExistException(String message) {
    super(message);
  }
}
