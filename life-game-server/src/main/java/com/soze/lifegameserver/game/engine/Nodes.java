package com.soze.lifegameserver.game.engine;

import com.soze.klecs.node.Node;
import com.soze.lifegameserver.game.engine.component.HarvesterComponent;
import com.soze.lifegameserver.game.engine.component.MovementComponent;
import com.soze.lifegameserver.game.engine.component.PhysicsComponent;
import com.soze.lifegameserver.game.engine.component.StorageComponent;

public interface Nodes {
  
  Node HARVESTER = Node.of(PhysicsComponent.class, MovementComponent.class, HarvesterComponent.class,
                           StorageComponent.class);
  Node MOVEMENT = Node.of(PhysicsComponent.class, MovementComponent.class);
  
}
