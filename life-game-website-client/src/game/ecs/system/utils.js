/**
 * Finds all entities which have all given component types.
 */
export const getEntities = (entityEngine, types) => {
  const entities = entityEngine.getEntities();

  return Object.values(entities).filter(entity => {
    return types.every(type => entity.getComponent(type));
  });
};
