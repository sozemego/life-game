export const createEntityEngine = () => {
  const entities = {};
  const systems = [];

  const addEntity = entity => {
    const { id } = entity;
    if (!id) {
      throw new Error('Entity needs id property');
    }
    entities[id] = entity;
  };

  const addSystem = system => {
    systems.push(system);
  };

  const update = delta => {
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
