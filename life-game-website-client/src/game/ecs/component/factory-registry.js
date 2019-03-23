import { TYPES } from "./types";

const factories = {};

export const getFactories = () => {
  console.log(factories);
  return factories;
};

const createName = (component, context) => {
  return { ...component };
};

factories[TYPES.NAME] = createName;

const createPhysics = (component, context) => {
  return {...component };
};

factories[TYPES.PHYSICS] = createPhysics;

const createGraphics = (component, context) => {
  return {...component}
};

factories[TYPES.GRAPHICS] = createGraphics;

const createResourceProvider = (component, context) => {
  return { ...component };
};

factories[TYPES.RESOURCE_PROVIDER] = createResourceProvider;
