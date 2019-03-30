import { setLoadGameMessage } from './actions';
import { createEngine } from './engine';
import { createInputHandler } from './input-handler';
import { createEntityEngine } from './ecs/entity-engine';
import { getFactories } from './ecs/component/factory-registry';
import { createEntity } from './ecs/entity';
import { createGraphicsSystem } from './ecs/system/graphics-system';
import { createTooltipSystem } from './ecs/system/tooltip-system';
import { createSelectSystem } from "./ecs/system/select-system";

export const createGameService = (client, dispatch, getState) => {
  const service = {};
  const tileSize = 1;
  const container = document.getElementById('game-container');
  const inputHandler = createInputHandler(container);

  let gfxEngine = null;
  let entityEngine = null;

  service.start = () => {
    dispatch(setLoadGameMessage('CONNECTING'));

    return client.connect().then(() => {
      dispatch(setLoadGameMessage('AUTHENTICATING'));
      client.authorize();

      client.onMessage('AUTHORIZED', msg => {
        dispatch(setLoadGameMessage('AUTHENTICATED'));
        createGame(client);
      });
    });
  };

  const createGame = () => {
    dispatch(setLoadGameMessage('CREATING GAME ENGINE'));
    gfxEngine = createEngine(inputHandler, tileSize);
    gfxEngine.start();

    dispatch(setLoadGameMessage('REQUESTING GAME WORLD'));

    client.requestGameWorld();

    client.onMessage('WORLD', msg => {
      dispatch(setLoadGameMessage('RENDERING GAME WORLD'));
      gfxEngine.setWorld(msg.world);
      setTimeout(() => {
        dispatch(setLoadGameMessage(null));
      }, 0);
    });

    entityEngine = createEntityEngine();
    entityEngine.addSystem(createGraphicsSystem(entityEngine, gfxEngine));
    entityEngine.addSystem(createTooltipSystem(entityEngine, gfxEngine));
    entityEngine.addSystem(createSelectSystem(entityEngine, gfxEngine));

    client.onMessage('ENTITY', msg => {
      console.log(msg);
      msg.dtos.forEach(addEntity);
    });

    gfxEngine.update = () => {
      entityEngine.update(1 / 60);
    };
  };

  const addEntity = dto => {
    const { id, components } = dto;
    const entity = createEntity(id);
    const factories = getFactories();

    const context = {
      entity,
      engine: gfxEngine,
    };

    Object.entries(components)
      .map(([type, component]) => factories[type](component, context))
      .forEach(entity.addComponent);

    entityEngine.addEntity(entity);
  };

  service.destroy = () => {
    console.log('Destroying game service');
    dispatch(setLoadGameMessage('RELEASING RESOURCES'));
    if (gfxEngine) gfxEngine.stop();
    if (client) client.disconnect();
    if (inputHandler) inputHandler.destroy();
  };

  return service;
};
