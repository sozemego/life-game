package com.soze.lifegameserver.game.handler;

import com.soze.klecs.entity.Entity;
import com.soze.lifegame.common.ws.message.client.TargetEntity;
import com.soze.lifegameserver.game.GameCoordinator;
import com.soze.lifegameserver.game.engine.GameEngine;
import com.soze.lifegameserver.game.engine.component.HarvesterComponent;
import com.soze.lifegameserver.game.engine.component.ResourceProviderComponent;
import com.soze.lifegameserver.game.engine.component.StorageComponent;
import com.soze.lifegameserver.game.entity.EntityCache;
import com.soze.lifegameserver.game.world.Resource;
import com.soze.lifegameserver.game.ws.GameSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Responsible for handling a message requesting one entity to target another.
 * The result of this depends on what source and target components.
 */
@Service
public class TargetEntityHandler {
  
  private static final Logger LOG = LoggerFactory.getLogger(TargetEntityHandler.class);
  
  private final EntityCache entityCache;
  private final GameCoordinator gameCoordinator;
  
  @Autowired
  public TargetEntityHandler(EntityCache entityCache,
                             GameCoordinator gameCoordinator) {
    this.entityCache = entityCache;
    this.gameCoordinator = gameCoordinator;
  }
  
  @EventListener(condition = "#event.clientMessageType.equals('TARGET_ENTITY')")
  public void handleTargetEntity(ClientMessageEvent event) {
    handleTargetEntity(event.getSession(), (TargetEntity) event.getMessage());
  }
  
  private void handleTargetEntity(GameSession session, TargetEntity message) {
    LOG.info("User with session [{}] wants entity [{}] to target entity [{}] with action [{}]",
             session.getSession().getId(), message.getSourceEntityId(), message.getTargetEntityId(), message.getAction()
    );
    
    try {
      switch (message.getAction()) {
        case HARVEST:
          handleHarvest(session, message);
          break;
      }
    } catch (Exception e) {
      LOG.info("Exception while handling targeting type [{}]", message.getAction(), e);
    }
    
  }
  
  private void handleHarvest(GameSession session, TargetEntity message) {
    //1. get both entities
    Entity sourceEntity = getEntity(session.getUserId(), message.getSourceEntityId())
                            .orElseThrow(() -> new RuntimeException("SOURCE ENTITY DOES NOT EXIST"));
    Entity targetEntity = getEntity(session.getUserId(), message.getTargetEntityId())
                            .orElseThrow(() -> new RuntimeException("TARGET ENTITY DOES NOT EXIST"));
    
    //2. check if source is a harvester
    HarvesterComponent harvesterComponent = sourceEntity.getComponent(HarvesterComponent.class);
    if (harvesterComponent == null) {
      throw new RuntimeException("Source entity is not a harvester");
    }
    //3. check if target is a resource provider
    ResourceProviderComponent resourceProviderComponent = targetEntity.getComponent(ResourceProviderComponent.class);
    if (resourceProviderComponent == null) {
      throw new RuntimeException("Target entity is not a resource provider");
    }
  
    StorageComponent storage = sourceEntity.getComponent(StorageComponent.class);
    if (storage.isFull()) {
      throw new RuntimeException("Source entity has no free space!");
    }
    
    Resource resource = storage.getResource();
    if (resource != null) {
      Resource providedResource = resourceProviderComponent.getResource();
      if (resource != providedResource) {
        storage.setResource(null);
        storage.setCapacityTaken(0);
      }
    }
    
    GameEngine gameEngine = getGameEngine(session.getUserId());
    LOG.info("Adding harvest task to game engine");
  
    gameEngine.addTask(() -> {
      harvesterComponent.setHarvestingProgress(0f);
      harvesterComponent.setTargetId((Long) targetEntity.getId());
    });
    
  }
  
  private Optional<Entity> getEntity(long userId, long entityId) {
    return Optional.ofNullable(entityCache.getEntities(userId).get(entityId));
  }
  
  private GameEngine getGameEngine(long userId) {
    return gameCoordinator.getGameEngineByUserId(userId);
  }
  
}
