package com.soze.lifegameserver.game.entity;

import com.soze.klecs.engine.AddedEntityEvent;
import com.soze.klecs.engine.Engine;
import com.soze.klecs.engine.RemovedEntityEvent;
import com.soze.klecs.entity.Entity;
import com.soze.lifegameserver.game.engine.GameEngine;
import com.soze.lifegameserver.game.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EntityCache {

  private static final Logger LOG = LoggerFactory.getLogger(EntityCache.class);
  
  private final Map<Long, Map<Long, Entity>> entities = new ConcurrentHashMap<>();
  
  public void attachToEngine(World world, Engine engine) {
    final long worldId = world.getId();
    LOG.info("EntityCache attaching listeners for world with id [{}] and userId [{}]", worldId, world.getUserId());
    engine.addEntityEventListener((event) -> {
      if (event instanceof AddedEntityEvent) {
        entities.compute(worldId, (key, value) -> {
          if (value == null) {
            value = new HashMap<>();
          }
          value.put((Long) event.getEntity().getId(), event.getEntity());
          return value;
        });
      }
      if (event instanceof RemovedEntityEvent) {
        entities.computeIfPresent(worldId, (key, value) -> {
          value.remove(event.getEntity().getId());
          return value;
        });
      }
    });
  }
  
  public Map<Long, Entity> getEntities(long worldId) {
    return entities.getOrDefault(worldId, Collections.emptyMap());
  }

}
