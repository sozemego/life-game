import { makePayloadActionCreator } from '../store/utils';
import { createStatisticsClient } from './client';

export const action = 'what';

export const SET_STATISTICS_MESSAGE = 'SET_STATISTICS_MESSAGE';
export const setStatisticsMessage = makePayloadActionCreator(SET_STATISTICS_MESSAGE);

let client = null;

export const init = () => {
  return (dispatch, getState) => {
    dispatch(setStatisticsMessage("INITIALIZING STATISTICS"));

    client = createStatisticsClient();
    return client.connect()
      .then(() => {
        dispatch(setStatisticsMessage("CONNECTED"));
      }).catch(e => dispatch(setStatisticsMessage("ERROR WHILE CONNECTING")));
  };
};
