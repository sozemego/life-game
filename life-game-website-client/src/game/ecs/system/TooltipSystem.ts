import { getEntities } from './utils';
import { TYPES } from '../component/types';
import { EntityEngine, EntitySystem } from '../EntityEngine';
import { GameEngine } from '../../game-engine/GameEngine';
import { GfxEngine } from '../../gfx-engine/GfxEngine';
import { Sprite } from 'three';
import { Entity } from '../Entity';
import {
  GraphicsComponent,
  PhysicsComponent,
  ResourceProviderComponent,
} from '../component/FactoryRegistry';

export const createTooltipSystem = (
  gameEngine: GameEngine,
  entityEngine: EntityEngine,
  gfxEngine: GfxEngine,
): EntitySystem => {
  const resourceProviderTypes = [TYPES.PHYSICS, TYPES.GRAPHICS];

  let intersectedSprite: Sprite | null = null;
  let nextIntersectedSprite: Sprite | null = null;

  let cleanup = () => {};

  const update = (delta: number) => {
    nextIntersectedSprite = gfxEngine.getSpriteUnderMouse();
    if (intersectedSprite && intersectedSprite !== nextIntersectedSprite) {
      //clean previous intersected sprite
      cleanup();
    }
    if (!nextIntersectedSprite) {
      return;
    }
    const resourceProviders = getEntities(entityEngine, resourceProviderTypes);
    resourceProviders
      .filter(entity => {
        const graphics = entity.getComponent(TYPES.GRAPHICS) as GraphicsComponent;
        return graphics.sprite === nextIntersectedSprite;
      })
      .forEach(entity => updateEntity(delta, entity));
    intersectedSprite = nextIntersectedSprite;
  };

  const updateEntity = (delta: number, entity: Entity) => {
    const [graphics, physics, resourceProvider] = entity.getComponents([
      TYPES.GRAPHICS,
      TYPES.PHYSICS,
      TYPES.RESOURCE_PROVIDER,
    ]);
    const { x, y, width, height } = physics as PhysicsComponent;

    if (intersectedSprite !== nextIntersectedSprite) {
      const [group, removeGroup] = gfxEngine.createGroup(2);
      cleanup = () => {
        nextIntersectedSprite = null;
        intersectedSprite = null;
        removeGroup();
      };
      if (resourceProvider) {
        const { resource } = resourceProvider as ResourceProviderComponent;
        const resourceSprite = gfxEngine.createSprite(
          resource,
          {
            x: (x + width / 2) + 0.5,
            y: (y + height / 2) + 0.5,
          },
          group,
        );
        resourceSprite.renderOrder = 1;
        resourceSprite.material.depthTest = false;
        resourceSprite.scale.set(0.25, 0.25, 1);
      }
    } else {
    }
  };

  return {
    update,
  };
};
