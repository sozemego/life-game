package com.soze.lifegame.common.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ErrorResponse {

  private int statusCode = 0;
  private String errorMessage;
  private Map<String, Object> data = new HashMap<>();

  public ErrorResponse(int statusCode, String errorMessage) {
    this.statusCode = statusCode;
    this.errorMessage = errorMessage;
  }

  @JsonCreator
  public ErrorResponse(@JsonProperty("statusCode") int statusCode,
                       @JsonProperty("errorMessage") String errorMessage,
                       @JsonProperty("data") Map<String, Object> data) {
    this.statusCode = statusCode;
    this.errorMessage = errorMessage;
    this.data = data;
  }

  public Map<String, Object> addData(String key, Object value) {
    data.put(Objects.requireNonNull(key), value);
    return data;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }
}
