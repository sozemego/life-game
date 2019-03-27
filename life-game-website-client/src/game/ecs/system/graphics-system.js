import { TYPES } from '../component/types';
import { getEntities } from './utils';

export const createGraphicsSystem = (entityEngine, engine) => {
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

    sprite.position.x = x;
    sprite.position.y = y;
    sprite.scale.x = width;
    sprite.scale.y = height;
  };

  return {
    update,
  };
};
