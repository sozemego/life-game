package com.soze.lifegameserver.game.entity;

import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import com.soze.klecs.entity.EntityFactory;
import com.soze.lifegame.common.dto.world.EntityDto;
import com.soze.lifegame.common.json.JsonUtils;
import com.soze.lifegameserver.game.engine.component.GraphicsComponent;
import com.soze.lifegameserver.game.engine.component.NameComponent;
import com.soze.lifegameserver.game.engine.component.PhysicsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class EntityService {
  
  private final EntityRepository entityRepository;
  
  @Autowired
  public EntityService(EntityRepository entityRepository) {
    this.entityRepository = entityRepository;
  }
  
  public Entity convert(Engine engine, PersistentEntity persistentEntity) {
    EntityFactory entityFactory = engine.getEntityFactory();
    Entity entity = entityFactory.createEntity(persistentEntity.getId());
    
    entity.addComponent(new NameComponent(persistentEntity.getName()));
    entity.addComponent(persistentEntity.getPhysicsComponent());
    entity.addComponent(persistentEntity.getGraphicsComponent());
    
    return entity;
  }
  
  public EntityDto convert(Entity entity) {
    EntityDto dto = new EntityDto();
    dto.setId((Long) entity.getId());
    dto.setName(entity.getComponent(NameComponent.class).getName());
    dto.setGraphics(entity.getComponent(GraphicsComponent.class));
    dto.setPhysics(entity.getComponent(PhysicsComponent.class));
    return dto;
  }
  
  public List<EntityDto> convert(Collection<Entity> entities) {
    List<EntityDto> entityDtos = new ArrayList<>();
    for (Entity entity : entities) {
      entityDtos.add(convert(entity));
    }
    return entityDtos;
  }

  public PersistentEntity getTemplate(String name) {
    return entityRepository.getEntityTemplate(name);
  }

}
