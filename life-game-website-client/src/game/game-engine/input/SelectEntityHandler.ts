import { TYPES } from '../../ecs/component/types';
import { getEntities } from '../../ecs/system/utils';
import { GameEngine } from '../GameEngine';
import { Mouse } from '../../InputHandler';
import { Entity } from '../../ecs/Entity';
import { GraphicsComponent } from '../../ecs/component/FactoryRegistry';

export const createSelectEntityHandler = (gameEngine: GameEngine) => {
  return (mouse: Mouse) => {
    const { gfxEngine, entityEngine } = gameEngine;
    const { button } = mouse;

    if (!gfxEngine) {
      return false;
    }

    const clickedSprite = gfxEngine.getClickedSprite();
    const spriteUnderMouse = gfxEngine.getSpriteUnderMouse();
    const currentSelectedEntity = gameEngine.selectedEntity;

    if (!clickedSprite && !currentSelectedEntity) {
      return false;
    }

    if (currentSelectedEntity && !spriteUnderMouse && button === 2) {
      gameEngine.selectedEntity = null;
      return true;
    }

    // @ts-ignore
    const [selectedEntity] = getEntities(entityEngine, [TYPES.GRAPHICS]).filter(
      (entity: Entity) => {
        const graphics = entity.getComponent(TYPES.GRAPHICS) as GraphicsComponent;
        return graphics.sprite === clickedSprite;
      },
    );

    if (!selectedEntity) {
      return false;
    }

    gameEngine.selectedEntity = selectedEntity;

    return true;
  };
};
