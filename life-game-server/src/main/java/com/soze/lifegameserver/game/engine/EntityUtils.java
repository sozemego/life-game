package com.soze.lifegameserver.game.engine;

import com.soze.klecs.entity.Entity;
import com.soze.lifegameserver.game.engine.component.PhysicsComponent;

public class EntityUtils {
  
  public static float distance(Entity entity1, Entity entity2) {
    PhysicsComponent physics1 = entity1.getComponent(PhysicsComponent.class);
    PhysicsComponent physics2 = entity2.getComponent(PhysicsComponent.class);
    return distance(physics1, physics2);
  }
  
  public static float distance(PhysicsComponent physics1, PhysicsComponent physics2) {
    float x1 = physics1.getX();
    float y1 = physics1.getY();
    float x2 = physics2.getX();
    float y2 = physics2.getY();
    double ac = Math.abs(y2 - y1);
    double cb = Math.abs(x2 - x1);
    return (float) Math.hypot(ac, cb);
  }
  
  public static float angle(Entity entity1, Entity entity2) {
    PhysicsComponent physics1 = entity1.getComponent(PhysicsComponent.class);
    PhysicsComponent physics2 = entity2.getComponent(PhysicsComponent.class);
    return angle(physics1, physics2);
  }
  
  public static float angle(PhysicsComponent physics1, PhysicsComponent physics2) {
    float x1 = physics1.getX();
    float y1 = physics1.getY();
    float x2 = physics2.getX();
    float y2 = physics2.getY();
    float degrees = (float) Math.atan2(
      y2 - y1, x2 - x1
    );
    return degrees;
  }
  
}
