package com.soze.lifegameserver.dto;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Objects;

public class User {

  private static final String USER_NAME_CLAIM = "username";
  private static final String USER_ID_CLAIM = "user_id";

  private final DecodedJWT token;

  public User(DecodedJWT token) {
    this.token = Objects.requireNonNull(token);
  }

  public String getName() {
    return claim(USER_NAME_CLAIM).asString();
  }

  public Long getId() {
    return claim(USER_ID_CLAIM).asLong();
  }

  private Claim claim(String claim) {
    return token.getClaim(claim);
  }
}
