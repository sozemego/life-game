package com.soze.lifegame.common.ws.message.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

import static com.soze.lifegame.common.ws.message.client.ClientMessage.ClientMessageType.*;

public class AuthorizeMessage extends ClientMessage {

  private final String username;
  private final String token;

  @JsonCreator
  public AuthorizeMessage(@JsonProperty("messageId") final String messageId,
                          @JsonProperty("username") final String username,
                          @JsonProperty("token") final String token) {
    super(UUID.fromString(messageId), AUTHORIZE);
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
