package com.soze.lifegameserver.game.engine.component;

public class HarvesterComponent extends BaseComponent {
  
  public HarvesterComponent() {
    super(ComponentType.HARVESTER);
  }
  
  @Override
  public HarvesterComponent copy() {
    return new HarvesterComponent();
  }
}
