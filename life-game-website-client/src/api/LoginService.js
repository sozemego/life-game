import NetworkService, { clearAuthorizationToken, setAuthorizationToken } from './NetworkService';

const LOGIN = '/auth/login';
const REGISTER = '/register';

class LoginService extends NetworkService {
  constructor(baseUrl = null) {
    super(baseUrl);
  }

  login(username, password) {
    return this.post(LOGIN, { username, password })
      .then(({ jwt }) => {
        setAuthorizationToken(jwt);
        return jwt;
      })
      .catch(error => {
        if (error.status === 401) {
          throw new Error('Invalid username or password!');
        }
        throw error;
      });
  }

  register(username, password) {
    return this.post(REGISTER, { username, password }).catch(error => {
      clearAuthorizationToken();
      if (error.status === 400) {
        throw new Error(error.data.errorMessage);
      }
      throw error;
    });
  }

  logout() {
    clearAuthorizationToken();
    return Promise.resolve();
  }
}

export default LoginService;
