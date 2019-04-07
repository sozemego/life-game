package com.soze.lifegameserver.game.engine;

import com.soze.klecs.engine.Engine;
import com.soze.lifegameserver.game.world.World;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameEngine {
  
  private final World world;
  private final Engine engine;
  private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
  
  public GameEngine(World world, Engine engine) {
    this.world = Objects.requireNonNull(world);
    this.engine = Objects.requireNonNull(engine);
  }
  
  public void update(float delta) {
    Runnable task = null;
    while((task = tasks.poll()) != null) {
      task.run();
    }
    engine.setMetrics(true);
    engine.update(delta);
  }
  
  public long getUserId() {
    return world.getUserId();
  }
  
  public long getWorldId() {
    return world.getId();
  }
  
  public Timestamp getCreatedAt() {
    return world.getCreatedAt();
  }
  
  public Engine getEngine() {
    return engine;
  }
  
  public void addTask(Runnable runnable) {
    tasks.add(runnable);
  }
}
