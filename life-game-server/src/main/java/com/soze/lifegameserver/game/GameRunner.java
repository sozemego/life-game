package com.soze.lifegameserver.game;

import com.soze.lifegameserver.game.engine.GameEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameRunner implements Runnable {
  
  private static final Logger LOG = LoggerFactory.getLogger(GameRunner.class);
  
  private final List<GameEngine> engines = new CopyOnWriteArrayList<>();
  
  public void addGameEngine(GameEngine gameEngine) {
    Objects.requireNonNull(gameEngine);
    engines.add(gameEngine);
  }
  
  public List<GameEngine> getEngines() {
    return engines;
  }
  
  public void removeEngine(GameEngine gameEngine) {
    LOG.info("Removing engine for user id [{}]", gameEngine.getUserId());
    engines.remove(gameEngine);
  }
  
  @Override
  public void run() {
    while(true) {
      
      for (GameEngine engine : engines) {
        engine.update(1f / 60f);
      }
      
      try {
        Thread.sleep(15);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
