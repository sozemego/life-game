package com.soze.lifegameserver.game.ws;

import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import com.soze.lifegame.common.ws.message.client.ClientMessage;
import com.soze.lifegameserver.game.GameCoordinator;
import com.soze.lifegameserver.game.engine.GameEngine;
import com.soze.lifegameserver.game.engine.system.MovementSystem;
import com.soze.lifegameserver.game.engine.system.ResourceHarvesterSystem;
import com.soze.lifegameserver.game.entity.EntityCache;
import com.soze.lifegameserver.game.entity.EntityService;
import com.soze.lifegameserver.game.entity.PersistentEntity;
import com.soze.lifegameserver.game.handler.ClientMessageEvent;
import com.soze.lifegameserver.game.world.World;
import com.soze.lifegameserver.game.world.WorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class GameService {
  
  private static final Logger LOG = LoggerFactory.getLogger(GameService.class);
  
  private final ApplicationEventPublisher applicationEventPublisher;
  
  private final WorldService worldService;
  private final GameCoordinator gameCoordinator;
  private final EntityService entityService;
  private final EntityCache entityCache;
  
  @Autowired
  public GameService(ApplicationEventPublisher applicationEventPublisher,
                     WorldService worldService,
                     GameCoordinator gameCoordinator,
                     EntityService entityService,
                     EntityCache entityCache) {
    this.applicationEventPublisher = applicationEventPublisher;
    this.worldService = worldService;
    this.gameCoordinator = gameCoordinator;
    this.entityService = entityService;
    this.entityCache = entityCache;
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
  
  public void handleMessage(GameSession session, ClientMessage message) {
    applicationEventPublisher.publishEvent(new ClientMessageEvent(session, message));
  }
  
  public void addGameEngine(World world) {
    GameEngine gameEngine = createGameEngine(world);
    gameCoordinator.addGameEngine(gameEngine);
  }
  
  private GameEngine createGameEngine(World world) {
    Engine engine = new Engine();
    engine.addSystem(new MovementSystem(engine));
    engine.addSystem(new ResourceHarvesterSystem(engine));
    entityCache.attachToEngine(world, engine);
    LOG.info("Adding [{}] entities to GameEngine for world with userId [{}]", world.getEntities().size(), world.getUserId());
    for (PersistentEntity persistentEntity : world.getEntities()) {
      Entity entity = entityService.convert(engine, persistentEntity);
      engine.addEntity(entity);
    }
    GameEngine gameEngine = new GameEngine(world, engine);
    return gameEngine;
  }
  
}
