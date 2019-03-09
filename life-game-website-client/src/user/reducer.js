import { createReducer } from '../store/utils';
import { SET_TOKEN, SET_USER_NAME } from './action-types';

const setName = (state, action) => {
  localStorage.setItem('USER_NAME', action.payload);
  return { ...state, name: action.payload };
};

const setToken = (state, action) => {
  localStorage.setItem('TOKEN', action.payload);
  return { ...state, token: action.payload };
};

const getName = () => {
  return localStorage.getItem('USER_NAME') || null;
};

const getToken = () => {
  return localStorage.getItem('TOKEN') || null;
};

const initialState = {
  name: getName(),
  token: getToken(),
};

const reducer = createReducer(initialState, {
  [SET_USER_NAME]: setName,
  [SET_TOKEN]: setToken,
});

export default reducer;