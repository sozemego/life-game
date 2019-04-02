import { GameEngine } from '../GameEngine';
import { Mouse } from "../../InputHandler";

/**
 * Used to determine the cursor art on mouse move.
 */
export const createCursorHandler = (gameEngine: GameEngine) => {
  return (mouse: Mouse) => {
    const { gfxEngine } = gameEngine;

    if (!gfxEngine) {
      return false;
    }

    const hoveredSprite = gfxEngine.getSpriteUnderMouse();

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
