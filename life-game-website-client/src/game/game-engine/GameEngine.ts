import { createEntityEngine, EntityEngine } from '../ecs/EntityEngine';
import { getFactories } from '../ecs/component/FactoryRegistry';
import { createGfxEngine, GfxEngine } from "../gfx-engine/GfxEngine";
import { createSelectEntityHandler } from './input/SelectEntityHandler';
import { InputHandler } from '../InputHandler';
// @ts-ignore
import { createEntity, Entity } from '../ecs/Entity';
import { createGraphicsSystem } from "../ecs/system/GraphicsSystem";
import { createTooltipSystem } from "../ecs/system/TooltipSystem";
import { createSelectSystem } from "../ecs/system/SelectSystem";
import { createCursorHandler } from "./input/CursorHandler";
import { createTargetEntityHandler } from "./input/TargetEntityHandler";

const TILE_SIZE = 1;

export const createGameEngine = (inputHandler: InputHandler): GameEngine => {
  const gameEngine: GameEngine = {
    selectedEntity: null,
    gfxEngine: null,
    entityEngine: null,
    start: () => {},
    stop: () => {},
    setWorld: () => {},
    addEntity: () => {},
  };

  const updateCursor = createCursorHandler(gameEngine);

  gameEngine.start = () => {
    const gfxEngine = createGfxEngine(inputHandler, TILE_SIZE);
    gameEngine.gfxEngine = gfxEngine;

    const entityEngine = createEntityEngine();
    entityEngine.addSystem(createGraphicsSystem(gameEngine, entityEngine, gfxEngine));
    entityEngine.addSystem(createTooltipSystem(gameEngine, entityEngine, gfxEngine));
    entityEngine.addSystem(createSelectSystem(gameEngine, entityEngine, gfxEngine));

    gameEngine.entityEngine = entityEngine;

    // @ts-ignore
    gfxEngine.setUpdate(delta => {
      entityEngine.update(delta);
      updateCursor(gfxEngine.getMouse());
    });

    // @ts-ignore
    gfxEngine.start();

    inputHandler.onMouseUp(createSelectEntityHandler(gameEngine));
    inputHandler.onMouseUp(createTargetEntityHandler(gameEngine));

    // inputHandler.onMouseMove(createCursorHandler(gameEngine));
  };

  gameEngine.setWorld = world => {
    if (gameEngine.gfxEngine) {
      gameEngine.gfxEngine.setWorld(world);
    }
  };

  gameEngine.addEntity = dto => {
    const { id, components } = dto;
    const entity = createEntity(id);
    const factories = getFactories();

    const context = {
      entity,
      ...gameEngine,
    };

    Object.entries(components)
      // @ts-ignore
      .map(([type, component]) => factories[type](component, context))
      .forEach(entity.addComponent);

    // @ts-ignore
    gameEngine.entityEngine.addEntity(entity);
  };

  return gameEngine;
};

export interface GameEngine {
  selectedEntity: Entity | null;
  gfxEngine: GfxEngine | null;
  entityEngine: EntityEngine | null;
  start: () => void;
  stop: () => void;
  setWorld: (world: any) => void;
  addEntity: (entity: any) => void;
}
