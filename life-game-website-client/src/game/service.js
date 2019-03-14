import { setLoadGameMessage } from './actions';
import { createEngine } from './engine';
import { createInputHandler } from './view/input-handler';

/**
 *
 * @param {GameClient} client
 * @param dispatch
 * @param getState
 * @return GameService
 */
export const createGameService = (client, dispatch, getState) => {
  const service = {};
  const tileSize = 6;
  const container = document.getElementById('game-container');
  const inputHandler = createInputHandler(container);

  /**
   * @type {Engine}
   */
  let engine = null;

  service.start = () => {
    dispatch(setLoadGameMessage('CONNECTING'));

    return client.connect()
      .then(() => {
        dispatch(setLoadGameMessage('AUTHENTICATING'));
        client.authorize();

        client.onMessage('AUTHORIZED', (msg) => {
          dispatch(setLoadGameMessage('AUTHENTICATED'));
          createGame(client);
        });
      });
  };

  const createGame = () => {
    dispatch(setLoadGameMessage('CREATING GAME ENGINE'));
    engine = createEngine(inputHandler, tileSize);
    engine.start();

    dispatch(setLoadGameMessage('REQUESTING GAME WORLD'));

    client.requestGameWorld();

    client.onMessage('WORLD', (msg) => {
      dispatch(setLoadGameMessage('RENDERING GAME WORLD'));
      engine.setWorld(msg.world);
    });

    engine.update = () => {

    };
  };

  service.destroy = () => {
    console.log('Destroying game service');
    dispatch(setLoadGameMessage('RELEASING RESOURCES'));
    if (engine) engine.stop();
    if (client) client.disconnect();
    if (inputHandler) inputHandler.destroy();
  };


  return service;
};