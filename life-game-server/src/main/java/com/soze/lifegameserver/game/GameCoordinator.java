package com.soze.lifegameserver.game;

import com.soze.lifegameserver.game.engine.GameEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
  Object responsible for scheduling and running GameRunners.
  A GameRunner is a thread that can update one or more GameEngine instances (that actually simulate the game).
  This service takes care of assigning games to GameEngines to GameRunners, and decides
  how many GameEngines per runner should be assigned.
*/
@Service
public class GameCoordinator {
  
  private static final Logger LOG = LoggerFactory.getLogger(GameCoordinator.class);
  
  private final ExecutorService executorService = Executors.newFixedThreadPool(10);

  private final int enginesPerRunner = 4;
  
  private final List<GameRunner> gameRunners = new CopyOnWriteArrayList<>();
  
  /**
   * Adds {@link GameEngine} to one of the runners.
   */
  public void addGameEngine(GameEngine gameEngine) {
    Objects.requireNonNull(gameEngine);
    LOG.info("Adding game engine for user id [{}]", gameEngine.getUserId());
    GameRunner gameRunner = getGameRunner();
    executorService.submit(gameRunner);
    gameRunner.addGameEngine(gameEngine);
  }
  
  public List<GameRunner> getGameRunners() {
    return new ArrayList<>(gameRunners);
  }
  
  /**
   * Retrieves back the next GameRunner which can accommodate a GameEngine.
   * If none exist or all of them are at full capacity, a new one is created.
   */
  private GameRunner getGameRunner() {
    for (GameRunner gameRunner : gameRunners) {
      if (gameRunner.getEngines().size() < enginesPerRunner) {
        return gameRunner;
      }
    }
    GameRunner freshRunner = new GameRunner();
    gameRunners.add(freshRunner);
    LOG.info("Created a new GameRunner");
    return freshRunner;
  }
  
  public GameEngine getGameEngineByUserId(long userId) {
    for (GameRunner gameRunner : gameRunners) {
      for (GameEngine engine : gameRunner.getEngines()) {
        if (engine.getUserId() == userId) {
          return engine;
        }
      }
    }
    return null;
  }
  
  public void removeGameEngine(long userId) {
    for (GameRunner gameRunner : gameRunners) {
      for (GameEngine engine : gameRunner.getEngines()) {
        if (engine.getUserId() == userId) {
          gameRunner.removeEngine(engine);
        }
      }
    }
  }

}
