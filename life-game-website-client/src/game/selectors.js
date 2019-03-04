import { rootSelector } from '../store/utils';

const gameRoot = rootSelector('game');

export const isGameStarted = (state) => gameRoot(state).started;