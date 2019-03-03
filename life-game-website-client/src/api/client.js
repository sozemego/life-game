import axios from 'axios';
import store from '../store/store';
import { decrementFetchingActions, incrementFetchingActions } from '../app/actions';

const fetch = () => {
  store.dispatch(incrementFetchingActions());
};

const stopFetch = () => {
  store.dispatch(decrementFetchingActions());
};

const responseUnpacker = (response) => {
  const data = response.data;
  console.log(data);
  stopFetch()
  return data;
};

const errorUnpacker = (error) => {
  console.log(error.response);
  stopFetch()
  throw error.response;
};

const createClient = (baseUrl = null) => {
  const options = {
    baseURL: baseUrl,
  };

  const client = axios.create(options);

  client.interceptors.request.use(config => {
    fetch();
    return config;
  });
  client.interceptors.response.use(responseUnpacker, errorUnpacker);

  return client;
};

export default createClient;