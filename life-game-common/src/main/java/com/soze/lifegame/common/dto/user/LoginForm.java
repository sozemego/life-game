package com.soze.lifegame.common.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class LoginForm {

  private final String username;
  private final char[] password;

  @JsonCreator
  public LoginForm(@JsonProperty("username") final String username,
                   @JsonProperty("password") final char[] password) {
    this.username = Objects.requireNonNull(username);
    this.password = Objects.requireNonNull(password);
  }

  public String getUsername() {
    return username;
  }

  public char[] getPassword() {
    return password;
  }

  public void reset() {
    for (int i = 0; i < password.length; i++) {
      password[i] = 0;
    }
  }
}
