package com.soze.lifegameserver.game.engine;

import com.soze.klecs.engine.Engine;
import com.soze.lifegameserver.game.world.World;

import java.sql.Timestamp;
import java.util.Objects;

public class GameEngine {
  
  private final World world;
  private final Engine engine;
  
  public GameEngine(World world, Engine engine) {
    this.world = Objects.requireNonNull(world);
    this.engine = Objects.requireNonNull(engine);
  }
  
  public void update(float delta) {
    engine.update(delta);
  }
  
  public long getUserId() {
    return world.getUserId();
  }
  
  public Timestamp getCreatedAt() {
    return world.getCreatedAt();
  }
  
  public Engine getEngine() {
    return engine;
  }
}
