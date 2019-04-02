import { TYPES } from '../component/types';

export const createSelectSystem = (gameEngine, entityEngine, gfxEngine) => {
  let previousSelectedEntity = null;
  let cleanup = () => {};

  const update = delta => {
    const { selectedEntity } = gameEngine;

    if (previousSelectedEntity && previousSelectedEntity !== selectedEntity) {
      cleanup();
    }

    if (!selectedEntity) {
      return;
    }

    const [graphics, physics] = selectedEntity.getComponents([TYPES.GRAPHICS, TYPES.PHYSICS]);
    const selectedSprite = graphics.sprite;

    const { x, y, width, height } = physics;
    const spriteX = selectedSprite.position.x;
    const spriteY = selectedSprite.position.y;

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
      selectedSprite['userData']['panel'] = panel;
    } else {
      const panel = selectedSprite['userData']['panel'];
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
