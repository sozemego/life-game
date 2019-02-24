package com.soze.lifegame.common.ws.message.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class AuthorizedMessage extends ServerMessage {
  
  @JsonCreator
  public AuthorizedMessage(@JsonProperty("messageId") UUID messageId) {
    super(messageId, ServerMessageType.AUTHORIZED);
  }
  
}
