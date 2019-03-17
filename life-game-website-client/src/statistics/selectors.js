import { rootSelector } from '../store/utils';

const root = rootSelector('statistics');

export const getWorlds = (state) => {
  const worlds = root(state).worlds;
  return Object.values(worlds);
};

export const getMessage = (state) => root(state).message;