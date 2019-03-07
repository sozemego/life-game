import { createPayloadSetter, createReducer } from '../store/utils';
import { SET_TOKEN, SET_USER_NAME } from './action-types';

const initialState = {
  name: null,
  token: null,
};

const reducer = createReducer(initialState, {
  [SET_USER_NAME]: createPayloadSetter('name'),
  [SET_TOKEN]: createPayloadSetter('token'),
});

export default reducer;