import { rootSelector } from '../store/utils';

const appRoot = rootSelector('app');

export const isFetching = (state) => appRoot(state).fetchingActions > 0;