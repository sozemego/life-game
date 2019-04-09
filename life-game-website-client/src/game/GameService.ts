import { setLoadGameMessage } from './actions';
import { createInputHandler } from './InputHandler';
import { createGameEngine, GameEngine } from './game-engine/GameEngine';
import { GameClient } from './GameClient';

export const createGameService = (
  client: GameClient,
  dispatch: Function,
  getState: Function,
): GameService => {
  const container = document.getElementById('game-container');
  if (!container) {
    throw new Error('Game container needs to exist!');
  }
  const inputHandler = createInputHandler(container);

  let gameEngine: GameEngine | null = null;

  const start = () => {
    dispatch(setLoadGameMessage('CONNECTING'));

    return client.connect().then(() => {
      dispatch(setLoadGameMessage('AUTHENTICATING'));
      client.authorize();

      client.onMessage('AUTHORIZED', (msg: any) => {
        dispatch(setLoadGameMessage('AUTHENTICATED'));
        createGame();
      });
    });
  };

  const createGame = () => {
    dispatch(setLoadGameMessage('CREATING GAME ENGINE'));
    gameEngine = createGameEngine(inputHandler, client);
    gameEngine.start();

    dispatch(setLoadGameMessage('REQUESTING GAME WORLD'));

    client.requestGameWorld();

    client.onMessage('WORLD', (msg: any) => {
      dispatch(setLoadGameMessage('RENDERING GAME WORLD'));
      // @ts-ignore
      gameEngine.setWorld(msg.world);
      setTimeout(() => {
        dispatch(setLoadGameMessage(null));
      }, 0);
    });

    client.onMessage('ENTITY', (msg: any) => {
      console.log(msg);
      // @ts-ignore
      msg.dtos.forEach(gameEngine.addEntity);
    });

    client.onMessage('ENTITY_CHANGED', (msg: any) => {
      if (gameEngine) {
        const { changeType, data } = msg;
        gameEngine.onEntityChanged(changeType, msg.entityId, data);
      }
    });
  };

  const destroy = () => {
    console.log('Destroying game service');
    dispatch(setLoadGameMessage('RELEASING RESOURCES'));
    if (gameEngine) gameEngine.stop();
    if (client) client.disconnect();
    if (inputHandler) inputHandler.destroy();
  };

  return {
    start,
    destroy,
  };
};

export interface GameService {
  start: Function;
  destroy: Function;
}
