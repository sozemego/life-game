import { makePayloadActionCreator } from '../store/utils';
import { createGame } from './game';

export const GAME_STARTED = 'GAME_STARTED';
export const setGameStarted = makePayloadActionCreator(GAME_STARTED);

export const startGame = () => {
  return (dispatch, getState) => {
    console.log('STARTING GAME')
    dispatch(setGameStarted(true));

    const game = createGame();
    game.start();

  };
};