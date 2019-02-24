import { makePayloadActionCreator } from '../store/utils';
import { getLoginService } from '../api/ServiceFactory';

const loginService = getLoginService();

const setLoggedIn = makePayloadActionCreator('loggedIn');

export const register = (username, password) => {
  return (dispatch, getState) => {
    return loginService.register(username, password);
  };
};

export const login = (username, password) => {
  return (dispatch, getState) => {
    return loginService.login(username, password)
      .then(() => dispatch(setLoggedIn(true)));
  };
};

export const logout = () => {
  return (dispatch, getState) => {
    return loginService.logout()
      .then(() => dispatch(setLoggedIn(false)));
  };
}