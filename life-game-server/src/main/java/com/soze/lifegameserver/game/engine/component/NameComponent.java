package com.soze.lifegameserver.game.engine.component;

public class NameComponent extends BaseComponent {
  
  private final String name;
  
  public NameComponent(String name) {
    super(ComponentType.NAME);
    this.name = name;
  }
  
  public String getName() {
    return name;
  }
  
  @Override
  public NameComponent copy() {
    return new NameComponent(getName());
  }
}
