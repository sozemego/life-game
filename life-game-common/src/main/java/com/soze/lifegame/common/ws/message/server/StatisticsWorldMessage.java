package com.soze.lifegame.common.ws.message.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soze.lifegame.common.dto.world.StatisticsWorldDto;

import java.util.UUID;

public class StatisticsWorldMessage extends ServerMessage {
  
  private final StatisticsWorldDto worldDto;
  
  @JsonCreator
  public StatisticsWorldMessage(@JsonProperty UUID messageId, StatisticsWorldDto worldDto) {
    super(messageId, ServerMessageType.STATISTICS_WORLD);
    this.worldDto = worldDto;
  }
  
  public StatisticsWorldDto getWorldDto() {
    return worldDto;
  }
}
