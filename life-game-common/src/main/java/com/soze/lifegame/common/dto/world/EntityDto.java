package com.soze.lifegame.common.dto.world;

public class EntityDto {
  
  private long id;
  private String name;
  private String graphics;
  private String physics;
  
  public long getId() {
    return id;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getGraphics() {
    return graphics;
  }
  
  public void setGraphics(String graphics) {
    this.graphics = graphics;
  }
  
  public String getPhysics() {
    return physics;
  }
  
  public void setPhysics(String physics) {
    this.physics = physics;
  }
}
