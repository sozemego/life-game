package com.soze.lifegame.common.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class SimpleUserDto {

  private final String username;

  @JsonCreator
  public SimpleUserDto(@JsonProperty("username") final String username) {
    this.username = Objects.requireNonNull(username);
  }

  public String getUsername() {
    return username;
  }
}
