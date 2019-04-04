import { Mouse } from "../../InputHandler";
import { GameEngine } from "../GameEngine";
import { TYPES } from "../../ecs/component/types";

export const createTargetEntityHandler = (gameEngine: GameEngine) => {
  return (mouse: Mouse) => {

    const { gfxEngine, entityEngine } = gameEngine;
    const currentSelectedEntity = gameEngine.selectedEntity;

    if (!gfxEngine || !currentSelectedEntity || !entityEngine) {
      return false;
    }

    const { button } = mouse;

    if (button !== 2) {
      return false;
    }
    console.log('target entity handler !');

    const clickedSprite = gfxEngine.getSpriteUnderMouse();
    if (!clickedSprite) {
      return false;
    }

    // 0. find entity that was clicked
    const clickedEntity = entityEngine.getEntity(clickedSprite['userData']['entityId']);
    if (!clickedEntity) {
      return false;
    }

    //here we have to handle different types of components that can target other entities
    //1. harvester
    const harvester = currentSelectedEntity.getComponent(TYPES.HARVESTER);
    if (harvester) {

      //1a. check if targeted entity is a resource provider
      const targetResourceProvider = clickedEntity.getComponent(TYPES.RESOURCE_PROVIDER);
      if (targetResourceProvider) {
        console.log('CAN HARVEST')
      }

    }

    return true;
  };
};
