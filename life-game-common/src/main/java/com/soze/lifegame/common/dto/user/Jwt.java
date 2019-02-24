package com.soze.lifegame.common.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Jwt {

  private final String jwt;

  @JsonCreator
  public Jwt(@JsonProperty("jwt") final String jwt) {
    this.jwt = Objects.requireNonNull(jwt);
  }

  public String getJwt() {
    return jwt;
  }
}
