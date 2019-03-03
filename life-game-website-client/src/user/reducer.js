import { createReducer } from '../store/utils';

const initialState = {
  name: null
};

const reducer = createReducer(initialState);

export default reducer;