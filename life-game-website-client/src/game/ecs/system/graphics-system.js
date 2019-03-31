import { TYPES } from '../component/types';
import { getEntities } from './utils';

export const createGraphicsSystem = (gameEngine, entityEngine, gfxEngine) => {
  const types = [TYPES.GRAPHICS, TYPES.PHYSICS];

  const update = delta => {
    const entities = getEntities(entityEngine, types);

    entities.forEach(entity => {
      const components = types
        .map(type => entity.getComponent(type))
        .reduce((map, next) => {
          map[next.type] = next;
          return map;
        }, {});

      updateEntity(delta, entity, components);
    });
  };

  const updateEntity = (delta, entity, components) => {
    const physics = components[TYPES.PHYSICS];
    const graphics = components[TYPES.GRAPHICS];

    const { x, y, width, height } = physics;
    const { sprite } = graphics;

    if (width % 2 === 0) {
      sprite.position.x = x + width / 4;
    } else if (width > 1) {
      sprite.position.x = x + 1;
    } else {
      sprite.position.x = x;
    }

    if (height % 2 === 0) {
      sprite.position.y = y + height / 4;
    } else if (height > 1) {
      sprite.position.y = y + 1;
    } else {
      sprite.position.y = y;
    }

    sprite.scale.x = width;
    sprite.scale.y = height;
  };

  return {
    update,
  };
};
