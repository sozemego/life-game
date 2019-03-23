import { createPayloadSetter, createReducer } from '../store/utils';
import { GAME_STARTED, LOAD_GAME_MESSAGE } from './actions';

const initialState = {
  started: false,
  loadGameMessage: null,
};

export const reducer = createReducer(initialState, {
  [GAME_STARTED]: createPayloadSetter('started'),
  [LOAD_GAME_MESSAGE]: createPayloadSetter('loadGameMessage'),
});
