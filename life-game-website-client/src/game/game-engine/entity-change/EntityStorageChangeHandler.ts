import { GameEngine } from '../GameEngine';
import { StorageComponent } from "../../ecs/component/FactoryRegistry";
import { TYPES } from "../../ecs/component/types";


export type EntityStorageChangeHandler = (entityId: number, component: StorageComponent) => void;

export const createEntityStorageChangeHandler = (gameEngine: GameEngine): EntityStorageChangeHandler => {
  return (entityId: number, data: StorageComponent) => {
    const { entityEngine } = gameEngine;
    if (!entityEngine) {
      return;
    }
    const entity = entityEngine.getEntity(entityId);
    if (!entity) {
      return;
    }
    const storage = entity.getComponent(TYPES.STORAGE) as StorageComponent;
    storage.resource = data.resource;
    storage.capacityTaken = data.capacityTaken;
    storage.capacity = data.capacity;
  };
};
