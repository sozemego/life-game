import { createReducer } from '../store/utils';

const initialState = {
  worlds: [],
};

export const reducer = createReducer(initialState);