package com.soze.lifegameserver.tokenregistry.dto;

import java.util.Objects;

public class TokenRegistration {

  private final String username;
  private final String token;

  public TokenRegistration(String username, String token) {
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
