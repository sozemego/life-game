import { TYPES } from '../../ecs/component/types';
import { getEntities } from '../../ecs/system/utils';

export const createSelectEntityHandler = gameEngine => {
  return mouse => {
    const { gfxEngine, entityEngine } = gameEngine;

    const clickedSprite = gfxEngine.getClickedSprite();

    if (!clickedSprite) {
      return false;
    }

    const [selectedEntity] = getEntities(entityEngine, [TYPES.GRAPHICS]).filter(
      entity => entity.getComponent(TYPES.GRAPHICS).sprite === clickedSprite,
    );

    if (!selectedEntity) {
      return false;
    }

    gameEngine.selectedEntity = selectedEntity;

    return true;
  };
};
