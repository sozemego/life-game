import { GameEngine } from '../GameEngine';
import { ResourceHarvesterComponent } from "../../ecs/component/FactoryRegistry";
import { TYPES } from "../../ecs/component/types";


export type EntityHarvesterChangeHandler = (entityId: number, component: ResourceHarvesterComponent) => void;

export const createEntityHarvesterChangeHandler = (gameEngine: GameEngine): EntityHarvesterChangeHandler => {
  return (entityId: number, data: ResourceHarvesterComponent) => {
    const { entityEngine } = gameEngine;
    if (!entityEngine) {
      return;
    }
    const entity = entityEngine.getEntity(entityId);
    if (!entity) {
      return;
    }
    const harvester = entity.getComponent(TYPES.HARVESTER) as ResourceHarvesterComponent;
    harvester.targetEntityId = data.targetEntityId;
    harvester.harvestingProgress = data.harvestingProgress;
  };
};
