package com.soze.lifegameserver.game.engine.system;

import com.soze.klecs.engine.Engine;
import com.soze.klecs.system.EntitySystem;

public abstract class BaseEntitySystem implements EntitySystem {
  
  private final Engine engine;
  
  public BaseEntitySystem(Engine engine) {
    this.engine = engine;
  }
  
  @Override
  public Engine getEngine() {
    return engine;
  }
}
