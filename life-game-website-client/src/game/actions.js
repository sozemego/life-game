import { makePayloadActionCreator } from '../store/utils';
import { createGameClient } from './client';
import { getUser } from '../user/selectors';
import { createGameService } from './service';

export const GAME_STARTED = 'GAME_STARTED';
export const setGameStarted = makePayloadActionCreator(GAME_STARTED);

export const LOAD_GAME_MESSAGE = 'LOAD_GAME_MESSAGE';
export const setLoadGameMessage = makePayloadActionCreator(LOAD_GAME_MESSAGE);

let gameService = null;

export const startGame = () => {
  return (dispatch, getState) => {
    console.log('STARTING GAME');

    if (gameService != null) {
      gameService.destroy();
      gameService = null;
    }

    const user = getUser(getState);
    const client = createGameClient(user);
    gameService = createGameService(client, dispatch, getState);
    return gameService.start();
  };
};

export const stopGame = () => {
  return (dispatch, getState) => {
    if (gameService != null) {
      console.log('destroying game service');
      gameService.destroy();
      gameService = null;
    }
  };
};