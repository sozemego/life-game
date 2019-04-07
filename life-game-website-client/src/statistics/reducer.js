import { createPayloadSetter, createReducer } from '../store/utils';
import { FETCHED_ENGINES, SET_STATISTICS_MESSAGE } from './actions';

const initialState = {
  engines: {},
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

const fetchedEngines = (state, action) => {
  const { payload } = action;
  const { engines } = payload;
  const currentEngines = { ...state.engines };
  const nextEngines = {};

  Object.keys(currentEngines).forEach(key => {
    const engine = currentEngines[key];
    nextEngines[key] = { ...engine, running: false };
  });

  engines.forEach(engine => {
    const currentEngine = currentEngines[engine.username];
    nextEngines[engine.username] = { ...currentEngine, ...engine, running: true };
  });

  return { ...state, engines: nextEngines };
};

export const reducer = createReducer(initialState, {
  [SET_STATISTICS_MESSAGE]: createPayloadSetter('message'),
  [FETCHED_ENGINES]: fetchedEngines,
});
