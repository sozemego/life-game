import { setLoadGameMessage } from './actions';
import { createEngine } from './engine';

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

    dispatch(setLoadGameMessage('REQUESTING GAME WORLD'));

    client.requestGameWorld();

    client.onMessage('WORLD', (msg) => {
      dispatch(setLoadGameMessage('RENDERING GAME WORLD'));
      engine.setWorld(msg.world);
    });
  };

  service.destroy = () => {
    dispatch(setLoadGameMessage('RELEASING RESOURCES'));
    engine.stop();
    client.disconnect();
  };

  return service;
};