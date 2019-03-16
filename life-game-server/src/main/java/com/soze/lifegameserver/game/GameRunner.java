package com.soze.lifegameserver.game;

import com.soze.lifegameserver.game.engine.GameEngine;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameRunner extends Thread {
  
  private final List<GameEngine> engines = new CopyOnWriteArrayList<>();
  
  public void addGameEngine(GameEngine gameEngine) {
    Objects.requireNonNull(gameEngine);
    engines.add(gameEngine);
  }
  
  public List<GameEngine> getEngines() {
    return engines;
  }
  
}
