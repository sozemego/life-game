package com.soze.lifegame.api;

public class ApiResponse {

  private final int statusCode;
  private final String body;

  public ApiResponse(int statusCode, String body) {
    this.statusCode = statusCode;
    this.body = body;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getBody() {
    return body;
  }

  @Override
  public String toString() {
    return "ApiResponse{" +
             "statusCode=" + statusCode +
             ", body='" + body + '\'' +
             '}';
  }
}
