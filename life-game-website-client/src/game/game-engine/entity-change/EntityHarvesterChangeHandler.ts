import { GameEngine } from '../GameEngine';
import { ResourceHarvesterComponent } from "../../ecs/component/FactoryRegistry";


export type EntityHarvesterChangeHandler = (entityId: number, component: ResourceHarvesterComponent) => void;

export const createEntityHarvesterChangeHandler = (gameEngine: GameEngine): EntityHarvesterChangeHandler => {
  return (entityId: number, data: ResourceHarvesterComponent) => {
    console.log(entityId);
    console.log(data);
  };
};
