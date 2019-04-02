import { TYPES } from '../component/types';
import { getEntities } from './utils';
import { EntityEngine, EntitySystem } from '../EntityEngine';
import { GameEngine } from '../../game-engine/GameEngine';
import { GfxEngine } from '../../gfx-engine/GfxEngine';
import { Entity } from '../Entity';
import { GraphicsComponent, PhysicsComponent } from '../component/FactoryRegistry';

export const createGraphicsSystem = (
  gameEngine: GameEngine,
  entityEngine: EntityEngine,
  gfxEngine: GfxEngine,
): EntitySystem => {
  const types = [TYPES.GRAPHICS, TYPES.PHYSICS];

  const update = (delta: number) => {
    const entities = getEntities(entityEngine, types);

    entities.forEach(entity => {
      updateEntity(delta, entity);
    });
  };

  const updateEntity = (delta: number, entity: Entity) => {
    const [graphics, physics] = entity.getComponents(types);

    const { x, y, width, height } = physics as PhysicsComponent;
    const { sprite } = graphics as GraphicsComponent;

    if (!sprite) {
      return;
    }

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
