package com.soze.lifegame.common.ws.message.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;
import java.util.UUID;

public class EntityMessage extends ServerMessage {
  
  private final Set<String> data;
  
  @JsonCreator
  public EntityMessage(@JsonProperty("messageId") UUID messageId,
                       @JsonProperty("data") Set<String> data) {
    super(messageId, ServerMessageType.ENTITY);
    this.data = data;
  }
  
  public Set<String> getData() {
    return data;
  }
}
