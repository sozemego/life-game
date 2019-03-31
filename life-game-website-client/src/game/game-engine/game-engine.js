import { createEntityEngine } from "../ecs/entity-engine";
import { createGraphicsSystem } from "../ecs/system/graphics-system";
import { createTooltipSystem } from "../ecs/system/tooltip-system";
import { createSelectSystem } from "../ecs/system/select-system";
import { createEntity } from "../ecs/entity";
import { getFactories } from "../ecs/component/factory-registry";
import { createGfxEngine } from "../gfx-engine/gfx-engine";
import { createSelectEntityHandler } from "./input/select-entity-handler";

const TILE_SIZE = 1;

export const createGameEngine = (inputHandler) => {

  const gameEngine = {
    selectedEntity: null,
    gfxEngine: null,
    entityEngine: null
  };

  gameEngine.start = () => {
    const gfxEngine = createGfxEngine(inputHandler, TILE_SIZE);
    gameEngine.gfxEngine = gfxEngine;

    const entityEngine = createEntityEngine();
    entityEngine.addSystem(createGraphicsSystem(gameEngine, entityEngine, gfxEngine));
    entityEngine.addSystem(createTooltipSystem(gameEngine, entityEngine, gfxEngine));
    entityEngine.addSystem(createSelectSystem(gameEngine, entityEngine, gfxEngine));

    gameEngine.entityEngine = entityEngine;

    gfxEngine.update = () => {
      entityEngine.update(1 / 60);
    };

    gfxEngine.start();

    inputHandler.onMouseUp(createSelectEntityHandler(gameEngine));
  };

  gameEngine.setWorld = (world) => {
    gameEngine.gfxEngine.setWorld(world);
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
      .map(([type, component]) => factories[type](component, context))
      .forEach(entity.addComponent);

    gameEngine.entityEngine.addEntity(entity);
  };



  return gameEngine;
};