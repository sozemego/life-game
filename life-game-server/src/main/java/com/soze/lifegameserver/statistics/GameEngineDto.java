package com.soze.lifegameserver.statistics;

import java.sql.Timestamp;

public class GameEngineDto {
  
  private final String username;
  private final Timestamp createdAt;
  
  public GameEngineDto(String username, Timestamp createdAt) {
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
