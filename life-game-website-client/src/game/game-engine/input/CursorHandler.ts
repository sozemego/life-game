import { GameEngine } from '../GameEngine';
import { Mouse } from '../../InputHandler';
// @ts-ignore
import { Cursor } from "../../gfx-engine/Cursor";

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
      // return false;
    }

    //many possibilities to chose a cursor
    //1. Nothing is selected and mouse hovers over nothing
    const selectedEntity = gameEngine.selectedEntity;
    if (!selectedEntity && !hoveredSprite) {
      gfxEngine.setCursor(Cursor.DEFAULT);
    } else if (!selectedEntity && hoveredSprite) {
      gfxEngine.setCursor(Cursor.SELECT);
    } else if (selectedEntity) {
      gfxEngine.setCursor(Cursor.TARGET);
    }

    return false;
  };
};
