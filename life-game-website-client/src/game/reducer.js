import { createReducer } from '../store/utils';
import { GAME_STARTED } from './actions';

const initialState = {
  started: false,
};

export const reducer = createReducer(initialState, {
  [GAME_STARTED]: (state, action) => ({ ...state, started: action.payload }),
});