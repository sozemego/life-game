package com.soze.lifegame.common.dto.world;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class StatisticsWorldDto {
  
  private final long userId;
  private final Timestamp createdAt;
  
  @JsonCreator
  public StatisticsWorldDto(@JsonProperty("userId") long userId,
                            @JsonProperty("createdAt") Timestamp createdAt) {
    this.userId = userId;
    this.createdAt = createdAt;
  }
  
  public long getUserId() {
    return userId;
  }
  
  public Timestamp getCreatedAt() {
    return createdAt;
  }
}
