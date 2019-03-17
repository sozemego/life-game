package com.soze.lifegameserver.game.entity;

import java.io.Serializable;

public class PersistentEntity implements Serializable {

  private String id;
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
}
