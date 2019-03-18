package com.soze.lifegame.common.ws.message.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soze.lifegame.common.dto.world.EntityDto;

import java.util.List;
import java.util.UUID;

public class EntityMessage extends ServerMessage {
  
  private final List<EntityDto> dtos;
  
  @JsonCreator
  public EntityMessage(@JsonProperty("messageId") UUID messageId,
                       @JsonProperty("dtos") List<EntityDto> dtos) {
    super(messageId, ServerMessageType.ENTITY);
    this.dtos = dtos;
  }
  
  public List<EntityDto> getDtos() {
    return dtos;
  }
}
