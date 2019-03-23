import { getEntities } from './utils';
import { TYPES } from '../component/types';

export const createTooltipSystem = (entityEngine, engine) => {
  const resourceProviderTypes = [TYPES.PHYSICS, TYPES.GRAPHICS];

  let intersectedSprite = null;

  const update = delta => {
    intersectedSprite = engine.getSpriteUnderMouse();
    if (!intersectedSprite) {
      return;
    }
    const resourceProviders = getEntities(entityEngine, resourceProviderTypes);
    resourceProviders.forEach(entity => updateEntity(delta, entity));
  };

  const updateEntity = (delta, entity) => {
    const graphics = entity.getComponent(TYPES.GRAPHICS);
    const { sprite } = graphics;
    if (sprite === intersectedSprite) {
      console.log('IM UNDER MOUSE!');
    }
  };

  return {
    update,
  };
};
