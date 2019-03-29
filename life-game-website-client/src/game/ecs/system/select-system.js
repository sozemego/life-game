import { TYPES } from '../component/types';
import { getEntities } from './utils';

export const createSelectSystem = (entityEngine, engine) => {
  const types = [TYPES.GRAPHICS, TYPES.PHYSICS];

  let selectedSprite = null;
  let currentSelectedSprite = null;
  let cleanup = () => {};

  const update = delta => {
    selectedSprite = engine.getClickedSprite();
    if (currentSelectedSprite && currentSelectedSprite !== selectedSprite) {
      cleanup();
    }

    if (!selectedSprite) {
      return;
    }

    const selectableEntities = getEntities(entityEngine, types);
    selectableEntities
      .filter(entity => {
        return entity.getComponent(TYPES.GRAPHICS).sprite === selectedSprite;
      })
      .forEach(updateEntity);
    currentSelectedSprite = selectedSprite;
  };

  const updateEntity = entity => {
    const [graphics, physics] = entity.getComponents([TYPES.GRAPHICS, TYPES.PHYSICS]);

    const { x, y, width, height } = physics;
    const spriteX = selectedSprite.position.x;
    const spriteY = selectedSprite.position.y;

    if (currentSelectedSprite !== selectedSprite) {
      //different selected than the last time!
      const [group, removeGroup] = engine.createGroup(2);

      cleanup = () => {
        selectedSprite = null;
        currentSelectedSprite = null;
        removeGroup();
      };
      const panel = engine.createSprite(
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
  };

  return {
    update,
  };
};
