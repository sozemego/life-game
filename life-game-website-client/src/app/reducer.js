import { createReducer } from '../store/utils';
import { DECREMENT_FETCHING_ACTIONS, INCREMENT_FETCHING_ACTIONS } from './actions';

const initialState = {
  fetchingActions: 0,
};

const incrementFetchingActions = (state, action) => {
  const previousFetchingActions = state.fetchingActions;
  return { ...state, fetchingActions: previousFetchingActions + 1 };
};

const decrementFetchingActions = (state, action) => {
  const previousFetchingActions = state.fetchingActions;
  return { ...state, fetchingActions: previousFetchingActions - 1 };
};

const reducer = createReducer(initialState, {
  [INCREMENT_FETCHING_ACTIONS]: incrementFetchingActions,
  [DECREMENT_FETCHING_ACTIONS]: decrementFetchingActions,
});

export default reducer;
