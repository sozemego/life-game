package com.soze.lifegame.common.ws.message.server;

import java.util.Objects;
import java.util.UUID;

public class EntityChangedMessage extends ServerMessage {
  
  private final Long entityId;
  private final Object component;
  
  public EntityChangedMessage(Long entityId, Object component) {
    super(UUID.randomUUID(), ServerMessageType.ENTITY_CHANGED);
    this.entityId = Objects.requireNonNull(entityId);
    this.component = Objects.requireNonNull(component);
  }
  
  public Long getEntityId() {
    return entityId;
  }
  
  public Object getComponent() {
    return component;
  }
}
