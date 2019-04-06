package com.soze.lifegameserver.game.engine.component;

public class MovementComponent extends BaseComponent {
  
  private float speed;
  private Long targetEntityId;
  
  public MovementComponent() {
    super(ComponentType.MOVEMENT);
  }
  
  public MovementComponent(float speed, Long targetEntityId) {
    super(ComponentType.MOVEMENT);
    this.speed = speed;
  }
  
  public float getSpeed() {
    return speed;
  }
  
  public void setSpeed(float speed) {
    this.speed = speed;
  }
  
  public Long getTargetEntityId() {
    return targetEntityId;
  }
  
  public void setTargetEntityId(Long targetEntityId) {
    this.targetEntityId = targetEntityId;
  }
  
  @Override
  public MovementComponent copy() {
    return new MovementComponent(getSpeed(), getTargetEntityId());
  }
}
