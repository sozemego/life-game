import { createPayloadSetter, createReducer } from '../store/utils';
import { SET_STATISTICS_MESSAGE } from './actions';

const initialState = {
  worlds: [],
  message: null,
};

export const reducer = createReducer(initialState, {
  [SET_STATISTICS_MESSAGE]: createPayloadSetter('message'),
});