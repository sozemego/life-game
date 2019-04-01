import { GameEngine } from "../GameEngine";

/**
 * Used to determine the cursor art on mouse move.
 */
export const createCursorHandler = (gameEngine: GameEngine) => {
  return (mouse: any) => {

    const { gfxEngine } = gameEngine;

    const hoveredSprite = gfxEngine.getClickedSprite();

    if (!hoveredSprite) {
      return false;
    }

    //many possibilities to chose a cursor
    //1. if no entity is selected, means we might want to select it
    const selectedEntity = gameEngine.selectedEntity;
    if (!selectedEntity) {

    }

    return true;
  };
};