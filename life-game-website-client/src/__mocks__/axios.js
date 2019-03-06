import axios from 'axios';

const mockAxios = {
  create: () => mockAxios
};

mockAxios.create = () => mockAxios;

const interceptors = {

};

const request = {

};

request.use = (cfg) => cfg;

interceptors.request = request;

mockAxios.interceptors = interceptors;

const response = {
  use: (response, error) => {}
}

mockAxios.interceptors.response = response;

mockAxios.post = jest.fn()

mockAxios.defaults = {
  headers: {}
};


export default mockAxios;