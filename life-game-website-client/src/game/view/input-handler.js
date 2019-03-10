import { createLogger } from '../../utils';

const LOG = createLogger('input-handler.js');

const KEY_DOWN = 'KEY_DOWN';
const KEY_UP = 'KEY_UP';

export const createInputHandler = () => {

  const listeners = {
    [KEY_DOWN]: [],
    [KEY_UP]: []
  };

  const subscribe = (type, fn) => {
    const typeListeners = listeners[type];
    typeListeners.push(fn);
    return unsubscribeCallback(typeListeners, fn);
  };

  const unsubscribeCallback = (listeners, fn) => () => {
    const index = listeners.findIndex(listener => listener === fn);
    if (index > -1) {
      listeners.splice(index, 1);
    }
  };

  const onKeyUp = (fn) => {
    return subscribe(KEY_UP, fn);
  };

  const onKeyDown = (fn) => {
    return subscribe(KEY_DOWN, fn);
  };

  const keydown = (event) => {
    const { key } = event;
    const typeListeners = listeners[KEY_DOWN];
    typeListeners.forEach(listener => listener(key));
  };

  window.addEventListener('keydown', keydown);

  const keyup = (event) => {
    const { key } = event;
    const typeListeners = listeners[KEY_UP];
    typeListeners.forEach(listener => listener(key));
  };

  window.addEventListener('keyup', keyup);

  const destroy = () => {
    LOG('Destroying input handler');
    window.removeEventListener('keydown', keydown);
    window.removeEventListener('keyup', keyup);
  };

  return {
    onKeyUp, onKeyDown, destroy
  };
};