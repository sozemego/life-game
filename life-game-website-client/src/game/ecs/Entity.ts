export const createEntity = (id: any): Entity => {
  const components: Components = {};

  const addComponent = (component: Component) => {
    components[component.type] = component;
  };

  const removeComponent = (type: string) => {
    const component = components[type];
    if (component) {
      component.disabled = true;
    }
  };

  const getComponent = (type: string) => {
    return components[type];
  };

  const getComponents = (types: string[]) => {
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

export interface Entity {
  id: any;
  components: object;
  addComponent: (component: Component) => void;
  removeComponent: (type: string) => void;
  getComponent: (type: string) => Component;
  getComponents: (types: string[]) => Component[];
}

export interface Component {
  type: string;
  disabled: boolean;
}

export interface Components {
  [type: string]: Component;
}
