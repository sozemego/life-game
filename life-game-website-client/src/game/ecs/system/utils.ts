/**
 * Finds all entities which have all given component types.
 */
import { EntityEngine } from '../EntityEngine';
import { Entity } from '../Entity';

export const getEntities = (entityEngine: EntityEngine, types: string[]): Entity[] => {
  const entities = entityEngine.getEntities();

  return Object.values(entities).filter(entity => {
    return types.every(type => !!entity.getComponent(type));
  });
};
