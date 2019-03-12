const KEY_DOWN = 'KEY_DOWN';
const KEY_UP = 'KEY_UP';
const MOUSE_WHEEL = 'MOUSE_WHEEL';
const MOUSE_MOVE = 'MOUSE_MOVE';

export const createInputHandler = (dom = window) => {
  console.log(`Creating input handler for ${dom}`);

  const listeners = {
    [KEY_DOWN]: [],
    [KEY_UP]: [],
    [MOUSE_WHEEL]: [],
    [MOUSE_MOVE]: []
  };

  const subscribe = (type, fn) => {
    const typeListeners = listeners[type];
    typeListeners.push(fn);
    console.log(`Subscribed ${type} ${dom}`)
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

  const keyup = (event) => {
    const { key } = event;
    const typeListeners = listeners[KEY_UP];
    typeListeners.forEach(listener => listener(key));
  };

  dom.addEventListener('keyup', keyup);

  const onKeyDown = (fn) => {
    return subscribe(KEY_DOWN, fn);
  };

  const keydown = (event) => {
    const { key } = event;
    const typeListeners = listeners[KEY_DOWN];
    typeListeners.forEach(listener => listener(key));
  };

  dom.addEventListener('keydown', keydown);

  const onMouseWheel = (fn) => {
    return subscribe(MOUSE_WHEEL, fn);
  };

  const mouseWheel = (event) => {
    const delta = Math.sign(event.deltaY);
    const typeListeners = listeners[MOUSE_WHEEL];
    typeListeners.forEach(listener => listener(delta));
  };

  dom.addEventListener('wheel', mouseWheel);

  const onMouseMove = (fn) => {
    return subscribe(MOUSE_MOVE, fn);
  };

  const mouseMove = (event) => {
    const boundingBox = event.target.getBoundingClientRect();
    const rawX = event.clientX - boundingBox.x;
    const rawY = event.clientY - Math.ceil(boundingBox.y);
    const mouse = {
      rawX,
      rawY,
      x: (rawX / (dom.innerWidth || dom.width)) * 2 - 1,
      y: - (rawY / (dom.innerHeight || dom.height)) * 2 + 1
    };
    const typeListeners = listeners[MOUSE_MOVE];
    typeListeners.forEach(listener => listener(mouse));
  };

  dom.addEventListener('mousemove', mouseMove);

  const destroy = () => {
    console.log('Destroying input handler');
    dom.removeEventListener('keydown', keydown);
    dom.removeEventListener('keyup', keyup);
    dom.removeEventListener('wheel', mouseWheel);
    dom.removeEventListener('mousemove', mouseMove);
    Object.keys(listeners).forEach(type => listeners[type] = []);
  };

  return {
    onKeyUp,
    onKeyDown,
    onMouseWheel,
    destroy,
    onMouseMove
  };
};