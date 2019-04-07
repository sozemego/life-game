package com.soze.lifegameserver.game.ws;

import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import com.soze.lifegame.common.ws.message.client.ClientMessage;
import com.soze.lifegameserver.game.GameCoordinator;
import com.soze.lifegameserver.game.SessionCache;
import com.soze.lifegameserver.game.engine.GameEngine;
import com.soze.lifegameserver.game.engine.system.MovementSystem;
import com.soze.lifegameserver.game.engine.system.ResourceHarvesterSystem;
import com.soze.lifegameserver.game.entity.EntityCache;
import com.soze.lifegameserver.game.entity.EntityService;
import com.soze.lifegameserver.game.entity.PersistentEntity;
import com.soze.lifegameserver.game.handler.ClientMessageEvent;
import com.soze.lifegameserver.game.world.World;
import com.soze.lifegameserver.game.world.WorldService;
import org.glassfish.jersey.internal.util.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
  
  private static final Logger LOG = LoggerFactory.getLogger(GameService.class);
  
  private final WorldService worldService;
  private final GameCoordinator gameCoordinator;
  private final EntityService entityService;
  private final EntityCache entityCache;
  private final SessionCache sessionCache;
  
  @Autowired
  public GameService(WorldService worldService,
                     GameCoordinator gameCoordinator,
                     EntityService entityService,
                     EntityCache entityCache,
                     SessionCache sessionCache) {
    this.worldService = worldService;
    this.gameCoordinator = gameCoordinator;
    this.entityService = entityService;
    this.entityCache = entityCache;
    this.sessionCache = sessionCache;
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
    Engine engine = new Engine();
    Producer<Optional<GameSession>> sessionProducer = () -> sessionCache.getSession(world.getId());
    engine.addSystem(new MovementSystem(engine, sessionProducer));
    engine.addSystem(new ResourceHarvesterSystem(engine));
    entityCache.attachToEngine(world, engine);
    LOG.info("Adding [{}] entities to GameEngine for world with userId [{}]", world.getEntities().size(),
             world.getUserId());
    for (PersistentEntity persistentEntity : world.getEntities()) {
      Entity entity = entityService.convert(engine, persistentEntity);
      engine.addEntity(entity);
    }
    GameEngine gameEngine = new GameEngine(world, engine);
    return gameEngine;
  }
  
}
