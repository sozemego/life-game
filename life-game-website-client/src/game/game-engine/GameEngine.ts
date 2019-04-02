import { createEntityEngine, EntityEngine } from "../ecs/EntityEngine";
import { createGraphicsSystem } from '../ecs/system/graphics-system';
import { createTooltipSystem } from '../ecs/system/tooltip-system';
import { createSelectSystem } from '../ecs/system/select-system';
import { getFactories } from '../ecs/component/factory-registry';
import { createGfxEngine } from '../gfx-engine/GfxEngine';
import { createSelectEntityHandler } from './input/select-entity-handler';
import { InputHandler } from "../InputHandler";
// @ts-ignore
import { createEntity, Entity } from "../ecs/Entity";

const TILE_SIZE = 1;

export const createGameEngine = (inputHandler: InputHandler): GameEngine => {

  const gameEngine: GameEngine = {
    selectedEntity: null,
    gfxEngine: null,
    entityEngine: null,
    start: () => {},
    setWorld: () => {},
    addEntity: () => {},
  };

  gameEngine.start = () => {
    const gfxEngine = createGfxEngine(inputHandler, TILE_SIZE);
    gameEngine.gfxEngine = gfxEngine;

    const entityEngine = createEntityEngine();
    entityEngine.addSystem(createGraphicsSystem(gameEngine, entityEngine, gfxEngine));
    entityEngine.addSystem(createTooltipSystem(gameEngine, entityEngine, gfxEngine));
    entityEngine.addSystem(createSelectSystem(gameEngine, entityEngine, gfxEngine));

    gameEngine.entityEngine = entityEngine;

    // @ts-ignore
    gfxEngine.setUpdate((delta) => {
      entityEngine.update(delta);
    });

    // @ts-ignore
    gfxEngine.start();

    inputHandler.onMouseUp(createSelectEntityHandler(gameEngine));
  };

  gameEngine.setWorld = world => {
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
  gfxEngine: any;
  entityEngine: EntityEngine | null;
  start: () => void;
  setWorld: (world: any) => void;
  addEntity: (entity: any) => void;
}
