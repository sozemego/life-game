import { Entity } from './Entity';

export const createEntityEngine = () => {
  const entities: EntityContainer = {};
  const systems: any[] = [];

  const addEntity = (entity: Entity) => {
    const { id } = entity;
    if (!id) {
      throw new Error('Entity needs id property');
    }
    entities[id] = entity;
  };

  const addSystem = (system: any) => {
    systems.push(system);
  };

  const update = (delta: number) => {
    for (let system of systems) {
      system.update(delta);
    }
  };

  const getEntities = () => entities;

  return {
    addEntity,
    addSystem,
    update,
    getEntities,
  };
};

export interface EntityEngine {
  addEntity: (entity: Entity) => void;
  addSystem: (system: any) => void;
  update: (delta: number) => void;
  getEntities: () => EntityContainer;
}

interface EntityContainer {
  [id: string]: Entity
}
