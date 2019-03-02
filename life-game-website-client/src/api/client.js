import axios from 'axios';

const responseUnpacker = (response) => {
  const data = response.data;
  console.log(data)
  return data;
};

const errorUnpacker = (error) => {
  console.log(error.response);
  throw error.response;
};

const createClient = (baseUrl = null) => {
  const options = {
    baseURL: baseUrl,
  };

  const client = axios.create(options);

  client.interceptors.response.use(responseUnpacker, errorUnpacker);

  return client;
};

export default createClient;