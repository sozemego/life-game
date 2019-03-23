import { rootSelector } from '../store/utils';

const userRoot = rootSelector('user');

export const isLoggedIn = state => userRoot(state).name != null;
export const getToken = state => userRoot(state).token;
export const getName = state => userRoot(state).name;

export const getUser = state => {
  return {
    name: getName(state),
    token: getToken(state)
  };
};
