import createClient from './client';
import axios from 'axios';

export const setAuthorizationToken = token => {
  axios.defaults.headers['Authorization'] = `Bearer ${token}`;
};

export const clearAuthorizationToken = () => {
  delete axios.defaults.headers['Authorization'];
};

class NetworkService {

  constructor(baseUrl = null) {
    this.client = createClient(baseUrl);
  }

  /**
   * @returns AxiosInstance
   */
  getClient() {
    return this.client;
  }

  get(url) {
    return this.getClient().get(url);
  }

  post(url, body = null) {
    return this.getClient().post(url, body);
  }

}

export default NetworkService;