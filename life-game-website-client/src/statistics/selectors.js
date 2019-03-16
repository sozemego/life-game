import { rootSelector } from '../store/utils';

const root = rootSelector('statistics');

export const getWorlds = (state) => root(state).worlds;