package com.soze.lifegame.common.ws.message.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

public class TargetEntity extends ClientMessage {
  
  private final Long sourceEntityId;
  private final Long targetEntityId;
  
  @JsonCreator
  public TargetEntity(@JsonProperty("messageId") UUID messageId,
                      @JsonProperty("sourceEntityId") Long sourceEntityId,
                      @JsonProperty("targetEntityId") Long targetEntityId) {
    super(messageId, ClientMessageType.TARGET_ENTITY);
    this.sourceEntityId = Objects.requireNonNull(sourceEntityId);
    this.targetEntityId = Objects.requireNonNull(targetEntityId);
  }
  
  public Long getSourceEntityId() {
    return sourceEntityId;
  }
  
  public Long getTargetEntityId() {
    return targetEntityId;
  }
}
