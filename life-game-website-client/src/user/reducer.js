import { createReducer } from '../store/utils';

const initialState = {
  loggedIn: false,
};

const reducer = createReducer(initialState);

export default reducer;