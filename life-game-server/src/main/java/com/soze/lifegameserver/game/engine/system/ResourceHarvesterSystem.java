package com.soze.lifegameserver.game.engine.system;

import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import com.soze.lifegameserver.game.engine.EntityUtils;
import com.soze.lifegameserver.game.engine.component.HarvesterComponent;
import com.soze.lifegameserver.game.engine.component.MovementComponent;
import com.soze.lifegameserver.game.engine.Nodes;

import java.util.List;
import java.util.Optional;

public class ResourceHarvesterSystem extends BaseEntitySystem {
  
  public ResourceHarvesterSystem(Engine engine) {
    super(engine);
  }
  
  @Override
  public void update(float delta) {
    List<Entity> entities = getEngine().getEntitiesByNode(Nodes.HARVESTER);
  
    for (Entity entity : entities) {
      update(entity, delta);
    }
  }
  
  private void update(Entity entity, float delta) {
    //1. check if entity has target
    HarvesterComponent harvester = entity.getComponent(HarvesterComponent.class);
    Long targetId = harvester.getTargetId();
    if (targetId == null) {
      return;
    }
    
    //1a. check if entity still exists
    Optional<Entity> targetOptional = getEngine().getEntityById(targetId);
    if (!targetOptional.isPresent()) {
      harvester.setTargetId(null);
      return;
    }
    
    Entity target = targetOptional.get();
    //2. if so, check if we arrived at the target
    float distance = EntityUtils.distance(entity, target);
    System.out.println(distance);
    if (distance > 0.1) {
      MovementComponent movement = entity.getComponent(MovementComponent.class);
      movement.setTargetEntityId(targetId);
      //3. if we did not arrive yet, continue moving towards target
      return;
    }
    
    //3. if we arrived, start harvesting
    //4. when harvesting is done, clear target.
    
  }
  
}
