package com.soze.lifegameserver.game.entity;

import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import com.soze.klecs.entity.EntityFactory;
import org.springframework.stereotype.Service;

@Service
public class EntityService {

  
  
  public Entity convert(Engine engine, PersistentEntity persistentEntity) {
    EntityFactory entityFactory = engine.getEntityFactory();
    Entity entity = entityFactory.createEntity(persistentEntity.getId());
    
    entity.addComponent(persistentEntity.getPhysicsComponent());
    entity.addComponent(persistentEntity.getPhysicsComponent());
    
    return entity;
  }


}
