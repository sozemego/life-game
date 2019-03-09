import { makePayloadActionCreator } from '../store/utils';
import { createGameClient } from './client';
import { getUser } from '../user/selectors';
import { createGame as gameFactory } from './game';
import { createGameService } from './service';

export const GAME_STARTED = 'GAME_STARTED';
export const setGameStarted = makePayloadActionCreator(GAME_STARTED);

export const LOAD_GAME_MESSAGE = 'LOAD_GAME_MESSAGE';
export const setLoadGameMessage = makePayloadActionCreator(LOAD_GAME_MESSAGE);

let gameService = null;

export const startGame = () => {
  return (dispatch, getState) => {
    console.log('STARTING GAME')

    if (gameService != null) {
      throw new Error('Game service already exists!');
    }
    //1 create client
    const user = getUser(getState);
    const client = createGameClient(user);
    gameService = createGameService(client, dispatch, getState);
    gameService.start()

    return gameService.start();

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
    const game = gameFactory(client);
    game.start();

    dispatch(setLoadGameMessage('REQUESTING GAME WORLD'));

    client.requestGameWorld();

    client.onMessage((msg) => {
      if (msg.type === 'WORLD') {
        dispatch(setLoadGameMessage('RENDERING GAME WORLD'));
        game.setWorld(msg.world);
      }
    });

  };
};