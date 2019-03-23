import { combineReducers } from 'redux';

import user from '../user/reducer';
import app from '../app/reducer';
import { reducer as game } from '../game/reducer';
import { reducer as statistics } from '../statistics/reducer';

export default combineReducers({
  app,
  user,
  game,
  statistics
});
