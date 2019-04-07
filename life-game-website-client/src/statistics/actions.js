import { makePayloadActionCreator } from '../store/utils';
import { createStatisticsClient } from './client';

export const SET_STATISTICS_MESSAGE = 'SET_STATISTICS_MESSAGE';
export const setStatisticsMessage = makePayloadActionCreator(SET_STATISTICS_MESSAGE);

export const FETCHED_ENGINES = 'FETCHED_ENGINES';
export const fetchedEngines = makePayloadActionCreator(FETCHED_ENGINES);

let client = null;

export const init = () => {
  return (dispatch, getState) => {
    dispatch(setStatisticsMessage('INITIALIZING STATISTICS'));

    client = createStatisticsClient();

    client.onMessage('STATISTICS_ENGINES', msg => {
      console.log(msg);
      dispatch(fetchedEngines(msg));
    });

    return client
      .connect()
      .then(() => {
        dispatch(setStatisticsMessage('CONNECTED'));
      })
      .catch(e => dispatch(setStatisticsMessage('ERROR WHILE CONNECTING')));
  };
};

export const disconnect = () => {
  return (dispatch, getState) => {
    if (client) {
      client.disconnect();
      client = null;
    }
  };
};
