package com.soze.lifegame.common.ws.message.server;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class EntityChangedMessage extends ServerMessage {
  
  private final Long entityId;
  private final ChangeType changeType;
  private final Map<String, Object> data;
  
  public EntityChangedMessage(Long entityId, ChangeType changeType, Map<String, Object> data) {
    super(UUID.randomUUID(), ServerMessageType.ENTITY_CHANGED);
    this.entityId = Objects.requireNonNull(entityId);
    this.changeType = Objects.requireNonNull(changeType);
    this.data = Objects.requireNonNull(data);
  }
  
  public Long getEntityId() {
    return entityId;
  }
  
  public Map<String, Object> getData() {
    return data;
  }
  
  public ChangeType getChangeType() {
    return changeType;
  }
  
  public enum ChangeType {
    POSITION
  }
}
