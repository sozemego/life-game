package com.soze.lifegame.common.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class TokenRegistration {

  private final String username;
  private final String token;

  @JsonCreator
  public TokenRegistration(@JsonProperty("username") final String username,
                           @JsonProperty("token") final String token) {
    this.username = Objects.requireNonNull(username);
    this.token = Objects.requireNonNull(token);
  }

  public String getUsername() {
    return username;
  }

  public String getToken() {
    return token;
  }
}
