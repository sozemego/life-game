package com.soze.lifegameserver.game.ws;

import com.soze.klecs.engine.Engine;
import com.soze.lifegameserver.game.GameCoordinator;
import com.soze.lifegameserver.game.engine.EngineFactory;
import com.soze.lifegameserver.game.engine.GameEngine;
import com.soze.lifegameserver.game.world.World;
import com.soze.lifegameserver.game.world.WorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class GameService {
  
  private static final Logger LOG = LoggerFactory.getLogger(GameService.class);
  
  private final WorldService worldService;
  private final GameCoordinator gameCoordinator;
  private final EngineFactory engineFactory;
  
  @Autowired
  public GameService(WorldService worldService,
                     GameCoordinator gameCoordinator,
                     EngineFactory engineFactory) {
    this.worldService = worldService;
    this.gameCoordinator = gameCoordinator;
    this.engineFactory = engineFactory;
  }
  
  @PostConstruct
  public void setup() {
    LOG.info("Loading all worlds");
    List<World> worlds = worldService.getAllWorlds();
    LOG.info("Retrieved [{}] worlds", worlds.size());
    for (World world : worlds) {
      if (world.getId() > 0) {
        addGameEngine(world);
      }
    }
  }
  
  public void addGameEngine(World world) {
    GameEngine gameEngine = createGameEngine(world);
    gameCoordinator.addGameEngine(gameEngine);
  }
  
  private GameEngine createGameEngine(World world) {
    Engine engine = engineFactory.createEngine(world);
    GameEngine gameEngine = new GameEngine(world, engine);
    return gameEngine;
  }
  
}
