package com.soze.lifegame.common.ws.message.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soze.lifegame.common.dto.world.WorldDto;

import java.util.UUID;

public class WorldMessage extends ServerMessage {
  
  private final WorldDto world;
  
  @JsonCreator
  public WorldMessage(@JsonProperty("messageId") UUID messageId,
                      @JsonProperty("world") WorldDto world) {
    super(messageId, ServerMessageType.WORLD);
    this.world = world;
  }
  
  public WorldDto getWorld() {
    return world;
  }
}
