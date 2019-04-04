import { TYPES } from './types';
import { Component, Entity } from '../Entity';
import { GfxEngine } from '../../gfx-engine/GfxEngine';
import { Sprite } from 'three';

const factories: ComponentFactories = {};

export const getFactories = () => {
  return factories;
};

const createName = (component: any, context: FactoryRegistryContext): NameComponent => {
  return { ...component };
};

factories[TYPES.NAME] = createName;

const createPhysics = (component: any, context: FactoryRegistryContext): PhysicsComponent => {
  return {...component};
};

factories[TYPES.PHYSICS] = createPhysics;

const createGraphics = (component: any, context: FactoryRegistryContext): GraphicsComponent => {
  const { gfxEngine } = context;
  const sprite = gfxEngine.createSprite(component.texture, { x: 0, y: 0 }, null);
  sprite['userData']['entityId'] = context.entity.id;
  return { ...component, sprite };
};

factories[TYPES.GRAPHICS] = createGraphics;

const createResourceProvider = (
  component: any,
  context: FactoryRegistryContext,
): ResourceProviderComponent => {
  return { ...component };
};

factories[TYPES.RESOURCE_PROVIDER] = createResourceProvider;

const createHarvester = (component: any, context: FactoryRegistryContext): Component => {
  return { ...component };
};

factories[TYPES.HARVESTER] = createHarvester;

export interface ComponentFactories {
  [type: string]: (component: any, context: FactoryRegistryContext) => ResourceHarvesterComponent;
}

export interface GraphicsComponent extends Component {
  sprite: Sprite | null;
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

export interface ResourceHarvesterComponent extends Component {}

export interface FactoryRegistryContext {
  entity: Entity;
  gfxEngine: GfxEngine;
}