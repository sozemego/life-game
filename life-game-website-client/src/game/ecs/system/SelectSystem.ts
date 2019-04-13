import { TYPES } from '../component/types';
import { EntityEngine, EntitySystem } from '../EntityEngine';
import { GameEngine } from '../../game-engine/GameEngine';
import { GfxEngine } from '../../gfx-engine/GfxEngine';
import { Entity } from '../Entity';
import { GraphicsComponent, PhysicsComponent } from '../component/FactoryRegistry';

export const createSelectSystem = (
  gameEngine: GameEngine,
  entityEngine: EntityEngine,
  gfxEngine: GfxEngine,
): EntitySystem => {
  let previousSelectedEntity: Entity | null = null;
  let cleanup = () => {};

  const update = (delta: number) => {
    const { selectedEntity } = gameEngine;

    if (previousSelectedEntity && previousSelectedEntity !== selectedEntity) {
      cleanup();
    }

    if (!selectedEntity) {
      return;
    }

    const [graphics, physics] = selectedEntity.getComponents([TYPES.GRAPHICS, TYPES.PHYSICS]);
    const { mesh: selectedMesh } = graphics as GraphicsComponent;
    if (!selectedMesh) {
      return;
    }

    const { x, y, width, height } = physics as PhysicsComponent;
    const spriteX = selectedMesh.position.x;
    const spriteY = selectedMesh.position.y;

    if (previousSelectedEntity !== selectedEntity) {
      //different selected than the last time!
      const [group, removeGroup] = gfxEngine.createGroup(2);

      cleanup = () => {
        previousSelectedEntity = null;
        removeGroup();
        cleanup = () => {};
      };
      const panel = gfxEngine.createSprite(
        'panel_blue_empty',
        {
          x: spriteX,
          y: spriteY,
        },
        group,
      );
      panel.renderOrder = 1;
      panel.scale.x = width;
      panel.scale.y = height;
      selectedMesh['userData']['panel'] = panel;
    } else {
      const panel = selectedMesh['userData']['panel'];
      //same as last time
      panel.renderOrder = 1;
      panel.scale.x = width;
      panel.scale.y = height;
      panel.position.x = spriteX;
      panel.position.y = spriteY;
    }

    previousSelectedEntity = selectedEntity;
  };

  return {
    update,
  };
};
