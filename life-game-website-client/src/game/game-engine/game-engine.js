import { createEntityEngine } from "../ecs/entity-engine";
import { createGraphicsSystem } from "../ecs/system/graphics-system";
import { createTooltipSystem } from "../ecs/system/tooltip-system";
import { createSelectSystem } from "../ecs/system/select-system";
import { createEntity } from "../ecs/entity";
import { getFactories } from "../ecs/component/factory-registry";
import { createGfxEngine } from "../gfx-engine/gfx-engine";

const TILE_SIZE = 1;

export const createGameEngine = (inputHandler) => {

  let gfxEngine = null;
  let entityEngine = null;

  const start = () => {
    gfxEngine = createGfxEngine(inputHandler, TILE_SIZE);

    entityEngine = createEntityEngine();
    entityEngine.addSystem(createGraphicsSystem(entityEngine, gfxEngine));
    entityEngine.addSystem(createTooltipSystem(entityEngine, gfxEngine));
    entityEngine.addSystem(createSelectSystem(entityEngine, gfxEngine));

    gfxEngine.update = () => {
      entityEngine.update(1 / 60);
    };

    gfxEngine.start();
  };

  const setWorld = (world) => {
    gfxEngine.setWorld(world);
  };

  const addEntity = dto => {
    const { id, components } = dto;
    const entity = createEntity(id);
    const factories = getFactories();

    const context = {
      entity,
      gfxEngine,
    };

    Object.entries(components)
      .map(([type, component]) => factories[type](component, context))
      .forEach(entity.addComponent);

    entityEngine.addEntity(entity);
  };



  return {
    start,
    setWorld,
    addEntity
  };
};