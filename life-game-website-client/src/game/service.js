import { setLoadGameMessage } from './actions';
import { createEngine } from './engine';
import { createInputHandler } from './view/input-handler';
import { createLogger } from '../utils';

const LOG = createLogger('service.js');

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
  /**
   * @type {Engine}
   */
  let engine = null;
  let inputHandler = null;

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
    engine = createEngine(tileSize);
    engine.start();

    inputHandler = createInputHandler();

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
    LOG('Destroying game service');
    dispatch(setLoadGameMessage('RELEASING RESOURCES'));
    engine.stop();
    client.disconnect();
    inputHandler.destroy();
  };


  return service;
};