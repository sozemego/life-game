package com.soze.lifegameserver.game.engine.component;

public class PhysicsComponent extends BaseComponent {

  private int x;
  private int y;
  private int width;
  private int height;
  
  public PhysicsComponent() {
    super(ComponentType.PHYSICS);
  }
  
  public PhysicsComponent(int x, int y, int width, int height) {
    this();
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
  
  public int getX() {
    return x;
  }
  
  public void setX(int x) {
    this.x = x;
  }
  
  public int getY() {
    return y;
  }
  
  public void setY(int y) {
    this.y = y;
  }
  
  public int getWidth() {
    return width;
  }
  
  public void setWidth(int width) {
    this.width = width;
  }
  
  public int getHeight() {
    return height;
  }
  
  public void setHeight(int height) {
    this.height = height;
  }
  
  @Override
  public PhysicsComponent copy() {
    return new PhysicsComponent(x, y, width, height);
  }
  
  @Override
  public String toString() {
    return "PhysicsComponent{" +
             "x=" + x +
             ", y=" + y +
             ", width=" + width +
             ", height=" + height +
             '}';
  }
}
