package com.soze.lifegameserver.game.engine.component;

public class HarvesterComponent extends BaseComponent {
  
  private long targetId;
  
  public HarvesterComponent() {
    super(ComponentType.HARVESTER);
  }
  
  public long getTargetId() {
    return targetId;
  }
  
  public void setTargetId(long targetId) {
    this.targetId = targetId;
  }
  
  @Override
  public HarvesterComponent copy() {
    return new HarvesterComponent();
  }
}
