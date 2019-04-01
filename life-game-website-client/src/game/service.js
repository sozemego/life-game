import { setLoadGameMessage } from './actions';
import { createInputHandler } from './InputHandler';
import { createGameEngine } from "./game-engine/GameEngine";

export const createGameService = (client, dispatch, getState) => {
  const service = {};
  const container = document.getElementById('game-container');
  const inputHandler = createInputHandler(container);

  let gameEngine = null;

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
    gameEngine = createGameEngine(inputHandler);
    gameEngine.start();

    dispatch(setLoadGameMessage('REQUESTING GAME WORLD'));

    client.requestGameWorld();

    client.onMessage('WORLD', msg => {
      dispatch(setLoadGameMessage('RENDERING GAME WORLD'));
      gameEngine.setWorld(msg.world);
      setTimeout(() => {
        dispatch(setLoadGameMessage(null));
      }, 0);
    });

    client.onMessage('ENTITY', msg => {
      console.log(msg);
      msg.dtos.forEach(gameEngine.addEntity);
    });

  };

  service.destroy = () => {
    console.log('Destroying game service');
    dispatch(setLoadGameMessage('RELEASING RESOURCES'));
    if (gameEngine) gameEngine.stop();
    if (client) client.disconnect();
    if (inputHandler) inputHandler.destroy();
  };

  return service;
};
