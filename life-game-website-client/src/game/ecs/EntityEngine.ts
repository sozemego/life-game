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

  const getEntity = (id: number) => getEntities()[id];

  return {
    addEntity,
    addSystem,
    update,
    getEntities,
    getEntity,
  };
};

export interface EntityEngine {
  addEntity: (entity: Entity) => void;
  addSystem: (system: any) => void;
  update: (delta: number) => void;
  getEntities: () => EntityContainer;
  getEntity: (id: number) => Entity | null;
}

interface EntityContainer {
  [id: number]: Entity;
}

export interface EntitySystem {
  update: (delta: number) => void;
}
