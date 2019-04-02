export const makeActionCreator = (type: string, ...argNames: string[]) => {
  return (...args: any[]) => {
    const action: any = {
      type,
    };
    argNames.forEach((item, index) => {
      action[argNames[index]] = args[index];
    });
    return action;
  };
};

export const makePayloadActionCreator = (type: string) => {
  return (arg: any) => {
    return {
      type,
      payload: arg,
    };
  };
};

export const makePayloadActionCreators = (...types: string[]) => {
  return types.map(makePayloadActionCreator);
};

/*
  !! USE ONLY FOR React.useReducer !!
  If a handler is given for an action.type, then it's used as a reducer.
  Otherwise checks if state with key 'action.type' is present
  and if so, returns new spread state, with value from 'action.payload'
  assigned to 'action.type'. Otherwise returns old state.
  Forces user to provide all possible fields in 'initial state' passed to reducers.
  If they don't, this reducer will return old state.
 */
export const createHookReducer = (initialState: object, handlers: any = {}) => {
  return (state: any = initialState, action: any) => {
    if (handlers.hasOwnProperty(action.type)) {
      return handlers[action.type](state, action);
    } else {
      const previousState = state[action.type];
      if (previousState != null) {
        return { ...state, [action.type]: action.payload };
      }
      return state;
    }
  };
};

export const createReducer = (initialState: object, handlers: any = {}) => {
  return (state = initialState, action: any) => {
    if (handlers.hasOwnProperty(action.type)) {
      return handlers[action.type](state, action);
    } else {
      return state;
    }
  };
};

export const rootSelector = (rootName: string) => {
  return (state: any) => {
    return typeof state === 'function' ? state()[rootName] : state[rootName];
  };
};

/**
 * Creates a setter used as a reducer.
 * Assumes action has property.payload
 */
export const createPayloadSetter = (property: string) => {
  return (state: object, action: PayloadAction) => ({
    ...state,
    [property]: action.payload,
  });
};

export interface PayloadAction {
  payload: any;
}
