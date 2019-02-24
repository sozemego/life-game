package com.soze.lifegameserver.game.world;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.Table;

@Embeddable
@Table(name = "tile")
public class Tile {
  
  @Id
  @Column(name = "id")
  private long id;
  
  @Column(name = "x")
  private long x;
  
  @Column(name = "y")
  private long y;
  
  @Column(name = "world_id")
  private long worldId;
  
  public long getId() {
    return id;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public long getX() {
    return x;
  }
  
  public void setX(long x) {
    this.x = x;
  }
  
  public long getY() {
    return y;
  }
  
  public void setY(long y) {
    this.y = y;
  }
  
  public long getWorldId() {
    return worldId;
  }
  
  public void setWorldId(long worldId) {
    this.worldId = worldId;
  }
}
