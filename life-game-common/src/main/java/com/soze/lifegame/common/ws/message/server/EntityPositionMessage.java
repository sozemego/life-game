package com.soze.lifegame.common.ws.message.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class EntityPositionMessage extends ServerMessage {
  
  private final Long entityId;
  private final float x;
  private final float y;
  
  @JsonCreator
  public EntityPositionMessage(@JsonProperty("messageId") UUID messageId,
                               @JsonProperty("entityId") Long entityId,
                               @JsonProperty("x") float x,
                               @JsonProperty("y") float y) {
    super(messageId, ServerMessageType.ENTITY_POSITION);
    this.entityId = entityId;
    this.x = x;
    this.y = y;
  }
  
  public Long getEntityId() {
    return entityId;
  }
  
  public float getX() {
    return x;
  }
  
  public float getY() {
    return y;
  }
}
