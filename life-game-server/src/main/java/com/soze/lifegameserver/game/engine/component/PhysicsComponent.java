package com.soze.lifegameserver.game.engine.component;

public class PhysicsComponent extends BaseComponent {

  private float x;
  private float y;
  private float width;
  private float height;
  
  public PhysicsComponent() {
    super(ComponentType.PHYSICS);
  }
  
  public PhysicsComponent(float x, float y, float width, float height) {
    this();
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
  
  public float getX() {
    return x;
  }
  
  public void setX(float x) {
    this.x = x;
  }
  
  public float getY() {
    return y;
  }
  
  public void setY(float y) {
    this.y = y;
  }
  
  public float getWidth() {
    return width;
  }
  
  public void setWidth(float width) {
    this.width = width;
  }
  
  public float getHeight() {
    return height;
  }
  
  public void setHeight(float height) {
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
