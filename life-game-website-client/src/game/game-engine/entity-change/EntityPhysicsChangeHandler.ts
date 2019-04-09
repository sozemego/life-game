import { GameEngine } from '../GameEngine';
import { TYPES } from "../../ecs/component/types";
import { PhysicsComponent } from "../../ecs/component/FactoryRegistry";

export type EntityPhysicsChangeHandler = (entityId: number, component: PhysicsComponent) => void;

export const createEntityPhysicsChangeHandler = (gameEngine: GameEngine): EntityPhysicsChangeHandler => {
  return (entityId: number, data: PhysicsComponent) => {
    const { entityEngine } = gameEngine;
    if (!entityEngine) {
      return;
    }
    const entity = entityEngine.getEntity(entityId);
    if (!entity) {
      return;
    }
    const physics = entity.getComponent(TYPES.PHYSICS) as PhysicsComponent;
    physics.x = data.x;
    physics.y = data.y;
  };
};
