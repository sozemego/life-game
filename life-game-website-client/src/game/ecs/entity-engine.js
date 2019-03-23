export const createEntityEngine = () => {
  const entities = {};
  const systems = {};

  const addEntity = entity => {
    const { id } = entity;
    if (!id) {
      throw new Error('Entity needs id property');
    }
  };
};
