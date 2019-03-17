package com.soze.lifegame.common.dto.world;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EntityDto {
  
  private final String data;
  
  @JsonCreator
  public EntityDto(@JsonProperty("data") String data) {
    this.data = data;
  }
  
  public String getData() {
    return data;
  }
}
