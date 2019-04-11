import { GameEngine } from '../../game-engine/GameEngine';
import { EntityEngine, EntitySystem } from '../EntityEngine';
import { GfxEngine } from '../../gfx-engine/GfxEngine';
import { TYPES } from '../component/types';
import { getEntities } from './utils';
import { Entity } from '../Entity';
import { Group, Sprite } from 'three';
import { PhysicsComponent, StorageComponent } from '../component/FactoryRegistry';

export const createStorageGfxSystem = (
  gameEngine: GameEngine,
  entityEngine: EntityEngine,
  gfxEngine: GfxEngine,
): EntitySystem => {
  const types = [TYPES.PHYSICS, TYPES.STORAGE];

  const displays: StorageDisplayMap = {};

  const update = (delta: number) => {
    const entities = getEntities(entityEngine, types);

    entities.forEach(entity => updateEntity(entity, delta));
  };

  const updateEntity = (entity: Entity, delta: number) => {
    const display = displays[entity.id];
    const storage = entity.getComponent(TYPES.STORAGE) as StorageComponent;
    const physics = entity.getComponent(TYPES.PHYSICS) as PhysicsComponent;
    const { x, y, width, height } = physics;
    if (display) {
      if (storage.capacityTaken === 0) {
        display.cleanup();
      } else {
        const { sprite, amount, cleanup } = display;
        sprite.position.set(x, y + 0.1, 0.5);
        sprite.scale.set(0.2, 0.2, 1);
      }
    } else {
      const { resource } = storage;
      if (storage.capacityTaken === 0 || resource === null) {
        return;
      }
      const [group, removeGroup] = gfxEngine.createGroup(2);
      const sprite = gfxEngine.createSprite(
        <string>storage.resource,
        { x, y, width: 0.25, height: 0.25, renderOrder: 1, depthTest: true },
        group,
      );
      sprite.material.depthWrite = false;

      displays[entity.id] = {
        sprite,
        amount: storage.capacityTaken,
        cleanup: removeGroup,
      };
    }
  };

  return {
    update,
  };
};

interface StorageDisplay {
  sprite: Sprite;
  amount: number;
  cleanup: Function;
}

interface StorageDisplayMap {
  [type: number]: StorageDisplay;
}
