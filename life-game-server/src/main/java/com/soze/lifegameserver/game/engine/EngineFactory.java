package com.soze.lifegameserver.game.engine;

import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import com.soze.lifegameserver.game.engine.system.MovementSystem;
import com.soze.lifegameserver.game.engine.system.ResourceHarvesterSystem;
import com.soze.lifegameserver.game.entity.EntityCache;
import com.soze.lifegameserver.game.entity.EntityService;
import com.soze.lifegameserver.game.entity.PersistentEntity;
import com.soze.lifegameserver.game.world.World;
import com.soze.lifegameserver.game.ws.GameSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EngineFactory {
  
  private static final Logger LOG = LoggerFactory.getLogger(EngineFactory.class);
  
  private final EntityCache entityCache;
  private final EntityService entityService;
  
  @Autowired
  public EngineFactory(EntityCache entityCache, EntityService entityService) {
    this.entityCache = entityCache;
    this.entityService = entityService;
  }
  
  public GameEngine createEngine(World world, GameSession gameSession) {
    Engine engine = new Engine();
    engine.addSystem(new MovementSystem(engine, gameSession));
    engine.addSystem(new ResourceHarvesterSystem(engine));
    entityCache.attachToEngine(gameSession.getUser().getId(), engine);
    LOG.info("Adding [{}] entities to GameEngine for world with userId [{}]", world.getEntities().size(),
             world.getUserId());
    for (PersistentEntity persistentEntity : world.getEntities()) {
      Entity entity = entityService.convert(engine, persistentEntity);
      engine.addEntity(entity);
    }
    return new GameEngine(world, engine);
  }
  
}
