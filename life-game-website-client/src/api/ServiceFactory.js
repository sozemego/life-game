import LoginService from './LoginService';

let loginService = null;

/**
 * @returns {LoginService}
 */
export const getLoginService = () =>
  loginService
    ? loginService
    : (loginService = new LoginService('http://localhost:8001/user'));
