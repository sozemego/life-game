import { makePayloadActionCreator } from '../store/utils';
import { createGameClient } from './client';
import { getUser } from '../user/selectors';
import { createGame as gameFactory } from './game';

export const GAME_STARTED = 'GAME_STARTED';
export const setGameStarted = makePayloadActionCreator(GAME_STARTED);

export const startGame = () => {
  return (dispatch, getState) => {
    console.log('STARTING GAME')

    const user = getUser(getState);
    //1 create client
    const client = createGameClient(user);
    //2 connect client



    return client.connect()
      .then(() => {
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
    const game = gameFactory(client);
    game.start();
  };
};