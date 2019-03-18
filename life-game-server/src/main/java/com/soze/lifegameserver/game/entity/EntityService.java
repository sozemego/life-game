package com.soze.lifegameserver.game.entity;

import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import com.soze.klecs.entity.EntityFactory;
import com.soze.lifegame.common.dto.world.EntityDto;
import com.soze.lifegame.common.json.JsonUtils;
import com.soze.lifegameserver.game.engine.component.GraphicsComponent;
import com.soze.lifegameserver.game.engine.component.NameComponent;
import com.soze.lifegameserver.game.engine.component.PhysicsComponent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EntityService {

  
  
  public Entity convert(Engine engine, PersistentEntity persistentEntity) {
    EntityFactory entityFactory = engine.getEntityFactory();
    Entity entity = entityFactory.createEntity(persistentEntity.getId());
    
    entity.addComponent(persistentEntity.getPhysicsComponent());
    entity.addComponent(persistentEntity.getPhysicsComponent());
    
    return entity;
  }
  
  public EntityDto convert(Entity entity) {
    EntityDto dto = new EntityDto();
    dto.setId((Long) entity.getId());
    dto.setName(entity.getComponent(NameComponent.class).getName());
    dto.setGraphics(JsonUtils.objectToJson(entity.getComponent(GraphicsComponent.class)));
    dto.setPhysics(JsonUtils.objectToJson(entity.getComponent(PhysicsComponent.class)));
    return dto;
  }
  
  public List<EntityDto> convert(Engine engine) {
    List<EntityDto> entityDtos = new ArrayList<>();
    for (Entity entity : engine.getAllEntities()) {
      entityDtos.add(convert(entity));
    }
    return entityDtos;
  }


}
