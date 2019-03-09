import { setLoadGameMessage } from './actions';
import { createEngine } from './engine';

/**
 *
 * @param client
 * @param dispatch
 * @param getState
 * @return GameService
 */
export const createGameService = (client, dispatch, getState) => {
  const service = {};

  service.start = () => {
    dispatch(setLoadGameMessage('CONNECTING'));

    return client.connect()
      .then(() => {
        dispatch(setLoadGameMessage('AUTHENTICATING'));
        client.authorize();

        client.onMessage((msg) => {
          if (msg.type === 'AUTHORIZED') {
            dispatch(setLoadGameMessage('AUTHENTICATED'));
            createGame(client);
          }
        });
      });
  };

  const createGame = () => {
    console.log('CREATING THE GAME ENGINE');
    dispatch(setLoadGameMessage('CREATING GAME ENGINE'));
    const engine = createEngine(client);
    engine.start();

    dispatch(setLoadGameMessage('REQUESTING GAME WORLD'));

    client.requestGameWorld();

    client.onMessage((msg) => {
      if (msg.type === 'WORLD') {
        dispatch(setLoadGameMessage('RENDERING GAME WORLD'));
        engine.setWorld(msg.world);
      }
    });
  };

  return service;
};