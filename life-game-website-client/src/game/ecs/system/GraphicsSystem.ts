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
    const { mesh } = graphics as GraphicsComponent;

    if (!mesh) {
      return;
    }

    if (width % 2 === 0) {
      mesh.position.x = x + width / 4;
    } else if (width > 1) {
      mesh.position.x = x + 1;
    } else {
      mesh.position.x = x;
    }

    if (height % 2 === 0) {
      mesh.position.y = y + height / 4;
    } else if (height > 1) {
      mesh.position.y = y + 1;
    } else {
      mesh.position.y = y;
    }

    mesh.scale.x = width;
    mesh.scale.y = height;
  };

  return {
    update,
  };
};
