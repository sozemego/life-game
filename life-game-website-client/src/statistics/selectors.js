import { rootSelector } from '../store/utils';

const root = rootSelector('statistics');

export const getWorlds = state => {
  return [];
};

export const getEngines = state => {
  const engines = root(state).engines;
  return Object.values(engines);
};

export const getMessage = state => root(state).message;
