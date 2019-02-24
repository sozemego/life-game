package com.soze.lifegameserver.game.world;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Table(name = "tile")
public class Tile {
  
  @Column(name = "x")
  private long x;
  
  @Column(name = "y")
  private long y;
  
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
  
}
