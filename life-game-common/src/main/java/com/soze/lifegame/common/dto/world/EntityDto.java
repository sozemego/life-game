package com.soze.lifegame.common.dto.world;

import java.util.HashMap;
import java.util.Map;

public class EntityDto {
  
  private long id;
  private Map<String, Object> components = new HashMap<>();
  
  public long getId() {
    return id;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void addComponent(String type, Object component) {
    components.put(type, component);
  }
  
  public Map<String, Object> getComponents() {
    return components;
  }
  
}