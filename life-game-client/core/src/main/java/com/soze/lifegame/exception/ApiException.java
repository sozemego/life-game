package com.soze.lifegame.exception;

import com.soze.lifegame.common.api.ErrorResponse;

public class ApiException extends RuntimeException {

  private final ErrorResponse errorResponse;

  public ApiException(ErrorResponse errorResponse) {
    this.errorResponse = errorResponse;
  }

  public ErrorResponse getError() {
    return errorResponse;
  }

  @Override
  public String toString() {
    return "ApiException{" +
             "errorResponse=" + errorResponse +
             "message=" + getMessage() +
             '}';
  }
}
