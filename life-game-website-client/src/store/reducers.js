import { combineReducers } from 'redux';

import user from '../user/reducer';
import app from '../app/reducer';
import { reducer as game } from '../game/reducer';

export default combineReducers({
  app,
  user,
  game,
});
