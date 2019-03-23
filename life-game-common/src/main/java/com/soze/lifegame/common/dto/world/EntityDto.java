package com.soze.lifegame.common.dto.world;

public class EntityDto {
  
  private long id;
  private String name;
  private Object graphics;
  private Object physics;
  
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
  
  public Object getGraphics() {
    return graphics;
  }
  
  public void setGraphics(Object graphics) {
    this.graphics = graphics;
  }
  
  public Object getPhysics() {
    return physics;
  }
  
  public void setPhysics(Object physics) {
    this.physics = physics;
  }
}
