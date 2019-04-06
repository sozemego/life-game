package com.soze.lifegameserver.game.handler;

import com.soze.klecs.entity.Entity;
import com.soze.lifegame.common.ws.message.client.TargetEntity;
import com.soze.lifegameserver.game.engine.component.HarvesterComponent;
import com.soze.lifegameserver.game.engine.component.ResourceProviderComponent;
import com.soze.lifegameserver.game.entity.EntityCache;
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
  
  @Autowired
  public TargetEntityHandler(EntityCache entityCache) {
    this.entityCache = entityCache;
  }
  
  @EventListener(condition = "#event.clientMessageType.equals('TARGET_ENTITY')")
  public void handleTargetEntity(ClientMessageEvent event) {
    handleTargetEntity(event.getSession(), (TargetEntity) event.getMessage());
  }
  
  private void handleTargetEntity(GameSession session, TargetEntity message) {
    LOG.info("User with session [{}] wants entity [{}] to target entity [{}] with action [{}]",
             session.getSession().getId(), message.getSourceEntityId(), message.getTargetEntityId(), message.getAction()
    );
    
    switch (message.getAction()) {
      case HARVEST:
        handleHarvest(session, message);
        break;
    }
    
  }
  
  private void handleHarvest(GameSession session, TargetEntity message) {
    //1. get both entities
    Entity sourceEntity = getEntity(session.getWorldId(), message.getSourceEntityId())
                            .orElseThrow(() -> new RuntimeException("SOURCE ENTITY DOES NOT EXIST"));
    Entity targetEntity = getEntity(session.getWorldId(), message.getTargetEntityId())
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
    
    LOG.info("Checks passed");
  }
  
  private Optional<Entity> getEntity(long worldId, long entityId) {
    return Optional.ofNullable(entityCache.getEntities(worldId).get(entityId));
  }
  
}
