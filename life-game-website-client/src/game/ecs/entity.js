export const createEntity = id => {
  const components = {};

  const addComponent = component => {
    components[component.type] = component;
  };

  const removeComponent = type => {
    const component = components[type];
    if (component) {
      component.disabled = true;
    }
  };

  const getComponent = type => {
    return components[type];
  };

  const getComponents = types => {
    return types.map(getComponent);
  };

  return {
    id,
    components,
    addComponent,
    removeComponent,
    getComponent,
    getComponents,
  };
};
