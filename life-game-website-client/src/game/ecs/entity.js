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

  return {
    id,
    components,
    addComponent,
    removeComponent,
  };
};
