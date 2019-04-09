import { GameEngine } from '../GameEngine';
import { TYPES } from "../../ecs/component/types";
import { PhysicsComponent } from "../../ecs/component/FactoryRegistry";
import { EntityChangeData } from "./EntityChangeHandler";

export interface PositionChangeData extends EntityChangeData {
  x: number;
  y: number;
}

export type EntityPositionChangeHandler = (entityId: number, data: PositionChangeData) => void;

export const createEntityPositionChangeHandler = (gameEngine: GameEngine): EntityPositionChangeHandler => {
  return (entityId: number, data: PositionChangeData) => {
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
