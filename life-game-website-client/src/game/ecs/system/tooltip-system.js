import { getEntities } from './utils';
import { TYPES } from '../component/types';

export const createTooltipSystem = (gameEngine, entityEngine, gfxEngine) => {
  const resourceProviderTypes = [TYPES.PHYSICS, TYPES.GRAPHICS];

  let intersectedSprite = null;
  let nextIntersectedSprite = null;

  let cleanup = () => {};

  const update = delta => {
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
      .filter(entity => entity.getComponent(TYPES.GRAPHICS).sprite === nextIntersectedSprite)
      .forEach(entity => updateEntity(delta, entity));
    intersectedSprite = nextIntersectedSprite;
  };

  const updateEntity = (delta, entity) => {
    const [graphics, physics, resourceProvider] = entity.getComponents([
      TYPES.GRAPHICS,
      TYPES.PHYSICS,
      TYPES.RESOURCE_PROVIDER,
    ]);
    const { x, y, width, height } = physics;

    if (intersectedSprite !== nextIntersectedSprite) {
      const [group, removeGroup] = gfxEngine.createGroup(2);
      cleanup = () => {
        nextIntersectedSprite = null;
        intersectedSprite = null;
        removeGroup();
      };
      if (resourceProvider) {
        const { resource } = resourceProvider;
        const resourceSprite = gfxEngine.createSprite(resource, {
          x: x + width / 2,
          y: y + height / 2,
        }, group);
        resourceSprite.renderOrder = 1;
        resourceSprite.material.depthTest = false;
        resourceSprite.scale.set(0.25, 0.25, 1);
        resourceSprite.position.x += 0.5;
        resourceSprite.position.y += 0.5;
      }
    }
  };

  return {
    update,
  };
};
