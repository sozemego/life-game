package com.soze.lifegameserver.game.world;

import java.io.Serializable;
import java.util.Objects;

public class Tile implements Serializable {
  
  private long x;
  
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
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Tile tile = (Tile) o;
    return x == tile.x &&
             y == tile.y;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}
