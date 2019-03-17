package com.soze.lifegame.common.dto.world;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class StatisticsWorldDto {
  
  private final String username;
  private final Timestamp createdAt;
  
  @JsonCreator
  public StatisticsWorldDto(@JsonProperty("username") String username,
                            @JsonProperty("createdAt") Timestamp createdAt) {
    this.username = username;
    this.createdAt = createdAt;
  }
  
  public String getUsername() {
    return username;
  }
  
  public Timestamp getCreatedAt() {
    return createdAt;
  }
}
