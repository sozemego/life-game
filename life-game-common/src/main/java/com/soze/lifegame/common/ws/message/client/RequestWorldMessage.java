package com.soze.lifegame.common.ws.message.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class RequestWorldMessage extends ClientMessage {
  
  @JsonCreator
  public RequestWorldMessage(@JsonProperty("messageId") UUID messageId) {
    super(messageId, ClientMessageType.REQUEST_WORLD);
  }
  
}

