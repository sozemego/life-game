import { rootSelector } from '../store/utils';

const root = rootSelector('statistics');

export const getWorlds = (state) => root(state).worlds;
export const getMessage = (state) => root(state).message;