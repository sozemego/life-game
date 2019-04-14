import { TYPES } from './types';
import { Component, Entity } from '../Entity';
import { GfxEngine } from '../../gfx-engine/GfxEngine';
import { Mesh, Sprite } from "three";
import { Resource } from './Resource';

const factories: ComponentFactories = {};

export const getFactories = () => {
  return factories;
};

const createName = (component: any, context: FactoryRegistryContext): NameComponent => {
  return { ...component };
};

factories[TYPES.NAME] = createName;

const createPhysics = (component: any, context: FactoryRegistryContext): PhysicsComponent => {
  return { ...component };
};

factories[TYPES.PHYSICS] = createPhysics;

const createGraphics = (component: any, context: FactoryRegistryContext): GraphicsComponent => {
  const { gfxEngine } = context;
  const mesh = gfxEngine.createMesh(component.texture, { x: 0, y: 0 }, null);
  mesh['userData']['entityId'] = context.entity.id;
  return { ...component, mesh };
};

factories[TYPES.GRAPHICS] = createGraphics;

const createResourceProvider = (
  component: any,
  context: FactoryRegistryContext,
): ResourceProviderComponent => {
  return { ...component };
};

factories[TYPES.RESOURCE_PROVIDER] = createResourceProvider;

const createHarvester = (
  component: any,
  context: FactoryRegistryContext,
): ResourceHarvesterComponent => {
  return { ...component };
};

factories[TYPES.HARVESTER] = createHarvester;

const createMovement = (component: any, context: FactoryRegistryContext): MovementComponent => {
  return { ...component };
};

factories[TYPES.MOVEMENT] = createMovement;

const createStorage = (component: any, context: FactoryRegistryContext): StorageComponent => {
  return { ...component };
};

factories[TYPES.STORAGE] = createStorage;

export interface ComponentFactories {
  [type: string]: (component: any, context: FactoryRegistryContext) => Component;
}

export interface GraphicsComponent extends Component {
  mesh: Mesh | null;
}

export interface NameComponent extends Component {
  name: string;
}

export interface PhysicsComponent extends Component {
  x: number;
  y: number;
  width: number;
  height: number;
}

export interface ResourceProviderComponent extends Component {
  resource: string;
}

export interface ResourceHarvesterComponent extends Component {
  targetEntityId?: number;
  harvestingProgress: number;
}

export interface MovementComponent extends Component {
  speed: number;
  targetEntityId?: number;
}

export interface StorageComponent extends Component {
  capacity: number;
  resource: Resource | null;
  capacityTaken: number;
}

export interface FactoryRegistryContext {
  entity: Entity;
  gfxEngine: GfxEngine;
}
