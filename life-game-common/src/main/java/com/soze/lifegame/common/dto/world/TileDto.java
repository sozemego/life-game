package com.soze.lifegame.common.dto.world;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TileDto {
  
  private final long x;
  private final long y;
  
  @JsonCreator
  public TileDto(@JsonProperty("x") long x,
                 @JsonProperty("y") long y) {
    this.x = x;
    this.y = y;
  }
  
  public long getX() {
    return x;
  }
  
  public long getY() {
    return y;
  }
}
