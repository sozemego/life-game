package com.soze.lifegame.common.dto.world;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.Set;

public class WorldDto {
  
  private final long id;
  private final Timestamp createdAt;
  private final Set<TileDto> tiles;
  
  @JsonCreator
  public WorldDto(@JsonProperty("id") long id,
                  @JsonProperty("createdAt") Timestamp createdAt,
                  @JsonProperty("tiles") Set<TileDto> tiles) {
    this.id = id;
    this.createdAt = createdAt;
    this.tiles = tiles;
  }
  
  public long getId() {
    return id;
  }
  
  public Timestamp getCreatedAt() {
    return createdAt;
  }
  
  public Set<TileDto> getTiles() {
    return tiles;
  }
}
