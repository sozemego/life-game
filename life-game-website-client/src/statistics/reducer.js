import { createPayloadSetter, createReducer } from '../store/utils';
import { ADD_WORLD, SET_STATISTICS_MESSAGE } from './actions';

const initialState = {
  worlds: {},
  message: null,
};

const addWorld = (state, action) => {
  const { payload } = action;
  const { username, createdAt } = payload.worldDto;
  const nextWorlds = { ...state.worlds };
  nextWorlds[username] = {
    username,
    createdAt,
  };
  return { ...state, worlds: nextWorlds };
};

export const reducer = createReducer(initialState, {
  [SET_STATISTICS_MESSAGE]: createPayloadSetter('message'),
  [ADD_WORLD]: addWorld,
});
