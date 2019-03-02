import { rootSelector } from '../store/utils';

const userRoot = rootSelector('user');

export const isLoggedIn = state => userRoot(state).loggedIn;