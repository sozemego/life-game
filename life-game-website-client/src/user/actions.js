import { getLoginService } from '../api/ServiceFactory';
import { history } from '../app/history';
import { makePayloadActionCreator } from '../store/utils';
import { SET_USER_NAME } from './action-types';

const loginService = getLoginService();

export const setName = makePayloadActionCreator(SET_USER_NAME);

export const register = (username, password) => {
  return (dispatch, getState) => {
    return loginService.register(username, password)
      .then(() => dispatch(login(username, password)));
  };
};

export const login = (username, password) => {
  return (dispatch, getState) => {
    return loginService.login(username, password)
      .then(() => dispatch(setName(username)))
      .then(() => setImmediate(() => history.push('/')));
  };
};

export const logout = () => {
  return (dispatch, getState) => {
    return loginService.logout()
      .then(() => dispatch(setName(null)));
  };
}