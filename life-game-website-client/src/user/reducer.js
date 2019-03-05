import { createReducer } from '../store/utils';
import { SET_USER_NAME } from './action-types';

const initialState = {
  name: null,
};

const setUserName = (state, action) => {
  return { ...state, name: action.payload };
};

const reducer = createReducer(initialState, {
  [SET_USER_NAME]: setUserName,
});

export default reducer;