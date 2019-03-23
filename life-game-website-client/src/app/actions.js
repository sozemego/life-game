import { makePayloadActionCreator } from '../store/utils';

export const INCREMENT_FETCHING_ACTIONS = 'INCREMENT_FETCHING_ACTIONS';
export const incrementFetchingActions = makePayloadActionCreator(INCREMENT_FETCHING_ACTIONS);

export const DECREMENT_FETCHING_ACTIONS = 'DECREMENT_FETCHING_ACTIONS';
export const decrementFetchingActions = makePayloadActionCreator(DECREMENT_FETCHING_ACTIONS);
