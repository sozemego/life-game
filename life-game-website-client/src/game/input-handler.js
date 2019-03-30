const KEY_DOWN = 'KEY_DOWN';
const KEY_UP = 'KEY_UP';
const MOUSE_WHEEL = 'MOUSE_WHEEL';
const MOUSE_MOVE = 'MOUSE_MOVE';
const MOUSE_UP = 'MOUSE_UP';

export const createInputHandler = (dom = window) => {
  console.log(`Creating input handler for ${dom}`);
  //not all elements can listen to key events. In case window is not passed
  //we want to use window to capture key events
  const keyListener = dom !== window ? window : dom;
  if (dom !== window) {
    console.log('passed DOM element is not window, window will be used for key capture.');
  }

  const listeners = {
    [KEY_DOWN]: [],
    [KEY_UP]: [],
    [MOUSE_WHEEL]: [],
    [MOUSE_MOVE]: [],
    [MOUSE_UP]: [],
  };

  const subscribe = (type, fn) => {
    const typeListeners = listeners[type];
    typeListeners.push(fn);
    console.log(`Subscribed ${type} ${dom}`);
    return unsubscribeCallback(typeListeners, fn);
  };

  const unsubscribeCallback = (listeners, fn) => () => {
    const index = listeners.findIndex(listener => listener === fn);
    if (index > -1) {
      listeners.splice(index, 1);
    }
  };

  const onKeyUp = fn => {
    return subscribe(KEY_UP, fn);
  };

  const keyup = event => {
    const { key } = event;
    const typeListeners = listeners[KEY_UP];
    typeListeners.forEach(listener => listener(key));
  };

  keyListener.addEventListener('keyup', keyup);

  const onKeyDown = fn => {
    return subscribe(KEY_DOWN, fn);
  };

  const keydown = event => {
    const { key } = event;
    const typeListeners = listeners[KEY_DOWN];
    typeListeners.forEach(listener => listener(key));
  };

  keyListener.addEventListener('keydown', keydown);

  const onMouseWheel = fn => {
    return subscribe(MOUSE_WHEEL, fn);
  };

  const mouseWheel = event => {
    const delta = Math.sign(event.deltaY);
    const typeListeners = listeners[MOUSE_WHEEL];
    typeListeners.forEach(listener => listener(delta));
  };

  dom.addEventListener('wheel', mouseWheel);

  const onMouseMove = fn => {
    return subscribe(MOUSE_MOVE, fn);
  };

  const mouseMove = event => {
    const boundingBox = event.target.getBoundingClientRect();
    const rawX = event.clientX - boundingBox.x;
    const rawY = event.clientY - Math.ceil(boundingBox.y);
    const mouse = {
      rawX,
      rawY,
      x: (rawX / (dom.innerWidth || Math.ceil(boundingBox.width))) * 2 - 1,
      y: -(rawY / (dom.innerHeight || Math.ceil(boundingBox.height))) * 2 + 1,
    };
    const typeListeners = listeners[MOUSE_MOVE];
    typeListeners.forEach(listener => listener(mouse));
  };

  dom.addEventListener('mousemove', mouseMove);

  const onMouseUp = fn => {
    return subscribe(MOUSE_UP, fn);
  };

  const mouseUp = event => {
    const boundingBox = event.target.getBoundingClientRect();
    const rawX = event.clientX - boundingBox.x;
    const rawY = event.clientY - Math.ceil(boundingBox.y);
    const mouse = {
      rawX,
      rawY,
      x: (rawX / (dom.innerWidth || Math.ceil(boundingBox.width))) * 2 - 1,
      y: -(rawY / (dom.innerHeight || Math.ceil(boundingBox.height))) * 2 + 1,
      button: event.button,
    };
    const typeListeners = listeners[MOUSE_UP];
    typeListeners.forEach(listener => listener(mouse));
  };

  dom.addEventListener('mouseup', mouseUp);

  const contextMenu = event => event.preventDefault();

  dom.addEventListener('contextmenu', contextMenu);

  const destroy = () => {
    console.log('Destroying input handler');
    keyListener.removeEventListener('keydown', keydown);
    keyListener.removeEventListener('keyup', keyup);
    dom.removeEventListener('wheel', mouseWheel);
    dom.removeEventListener('mousemove', mouseMove);
    dom.removeEventListener('mouseup', mouseUp);
    dom.removeEventListener('contextmenu', contextMenu);
    Object.keys(listeners).forEach(type => (listeners[type] = []));
  };

  return {
    onKeyUp,
    onKeyDown,
    onMouseWheel,
    destroy,
    onMouseMove,
    onMouseUp,
  };
};
