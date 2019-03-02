export const makeActionCreator = (type, ...argNames) => (...args) => {
  const action = {
    type,
  };
  argNames.forEach((item, index) => {
    action[argNames[index]] = args[index];
  });
  return action;
};

export const makePayloadActionCreator = (type) => (arg) => {
  return {
    type,
    payload: arg,
  };
};

export const makePayloadActionCreators = (...types) => {
  return types.map(makePayloadActionCreator);
};

/*
  Creates a reducer which checks if state with key 'action.type' is present
  and if so, returns new spread state, with value from 'action.payload'
  assigned to 'action.type'. Otherwise returns old state.
  Forces user to provide all possible fields in 'initial state' passed to reducers.
  If they don't, this reducer will return old state.
 */
const reducer = (state = {}, action) => {
  const previousState = state[action.type];
  if (previousState != null) {
    return { ...state, [action.type]: action.payload };
  }
  return state;
};

export const createReducer = (initialState, handlers = {}) => {
  return (state = initialState, action) => {
    if (handlers.hasOwnProperty(action.type)) {
      return handlers[action.type](state, action);
    } else {
      return reducer(state, action);
    }
  };
};

export const rootSelector = rootName => state => {
  return typeof state === 'function' ? state()[rootName] : state[rootName];
};