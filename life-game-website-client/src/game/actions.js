import { makePayloadActionCreator } from '../store/utils';
import { createGameClient } from './client';
import { getUser } from '../user/selectors';
import { createGame as gameFactory } from './game';

export const GAME_STARTED = 'GAME_STARTED';
export const setGameStarted = makePayloadActionCreator(GAME_STARTED);

export const LOAD_GAME_MESSAGE = 'LOAD_GAME_MESSAGE';
export const setLoadGameMessage = makePayloadActionCreator(LOAD_GAME_MESSAGE);

export const startGame = () => {
  return (dispatch, getState) => {
    console.log('STARTING GAME')

    const user = getUser(getState);
    //1 create client
    const client = createGameClient(user);
    //2 connect client

    dispatch(setLoadGameMessage('CONNECTING'));
    return client.connect()
      .then(() => {
        dispatch(setLoadGameMessage('AUTHENTICATING'));
        //3 start the connect to game world flow
        client.authorize();

        client.onMessage((msg) => {
          if (msg.type === 'AUTHORIZED') {
            //4 pass connected client to game
            dispatch(createGame(client));
          }
        });
      });
  };
};

export const createGame = (client) => {
  return (dispatch, getState) => {
    console.log('CREATING THE GAME ENGINE')
    dispatch(setLoadGameMessage('LOADING WORLD'));
    const game = gameFactory(client);
    game.start();
  };
};