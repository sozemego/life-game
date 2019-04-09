package com.soze.lifegameserver.game.engine.system;

import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import com.soze.lifegame.common.ws.message.server.EntityChangedMessage;
import com.soze.lifegame.common.ws.message.server.EntityChangedMessage.ChangeType;
import com.soze.lifegameserver.game.engine.EntityUtils;
import com.soze.lifegameserver.game.engine.Nodes;
import com.soze.lifegameserver.game.engine.component.MovementComponent;
import com.soze.lifegameserver.game.engine.component.PhysicsComponent;
import com.soze.lifegameserver.game.ws.GameSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MovementSystem extends BaseEntitySystem {
  
  private final GameSession gameSession;
  
  public MovementSystem(Engine engine, GameSession gameSession) {
    super(engine);
    this.gameSession = gameSession;
  }
  
  @Override
  public void update(float delta) {
    List<Entity> entities = getEngine().getEntitiesByNode(Nodes.MOVEMENT);
    entities.forEach(entity -> update(entity, delta));
  }
  
  private void update(Entity entity, float delta) {
    MovementComponent movement = entity.getComponent(MovementComponent.class);
    Long targetId = movement.getTargetEntityId();
    if (targetId == null) {
      return;
    }
    
    Optional<Entity> targetOptional = getEngine().getEntityById(targetId);
    if (!targetOptional.isPresent()) {
      movement.setTargetEntityId(null);
      return;
    }
    Entity target = targetOptional.get();
    
    float speed = movement.getSpeed();
    float angle = EntityUtils.angle(entity, target);
    float cos = (float) Math.cos(angle);
    float sin = (float) Math.sin(angle);
    
    PhysicsComponent physics = entity.getComponent(PhysicsComponent.class);
    float x = physics.getX();
    float y = physics.getY();
    
    physics.setX(x + (cos * speed * delta));
    physics.setY(y + (sin * speed * delta));
    
    Map<String, Object> data = new HashMap<>();
    data.put("x", physics.getX());
    data.put("y", physics.getY());
    gameSession.send(new EntityChangedMessage((Long) entity.getId(), ChangeType.POSITION, data));
  }
  
}
