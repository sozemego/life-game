package com.soze.lifegameserver.game.engine.component;

public class HarvesterComponent extends BaseComponent {
  
  private Long targetId;
  
  public HarvesterComponent() {
    super(ComponentType.HARVESTER);
  }
  
  public Long getTargetId() {
    return targetId;
  }
  
  public void setTargetId(Long targetId) {
    this.targetId = targetId;
  }
  
  @Override
  public HarvesterComponent copy() {
    return new HarvesterComponent();
  }
}
