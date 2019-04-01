const KEY_DOWN = 'KEY_DOWN';
const KEY_UP = 'KEY_UP';
const MOUSE_WHEEL = 'MOUSE_WHEEL';
const MOUSE_MOVE = 'MOUSE_MOVE';
const MOUSE_UP = 'MOUSE_UP';

export const createInputHandler = (dom = window): InputHandler => {
  console.log(`Creating input handler for ${dom}`);
  //not all elements can listen to key events. In case window is not passed
  //we want to use window to capture key events
  const keyListener = dom !== window ? window : dom;
  if (dom !== window) {
    console.log('passed DOM element is not window, window will be used for key capture.');
  }

  const listeners: ListenerContainer = {
    [KEY_DOWN]: [],
    [KEY_UP]: [],
    [MOUSE_WHEEL]: [],
    [MOUSE_MOVE]: [],
    [MOUSE_UP]: [],
  };

  const subscribe = (type: string, fn: Function) => {
    const typeListeners = listeners[type];
    typeListeners.push(fn);
    console.log(`Subscribed ${type} ${dom}`);
    return unsubscribeCallback(typeListeners, fn);
  };

  const unsubscribeCallback = (listeners: Function[], fn: Function) => () => {
    const index = listeners.findIndex(listener => listener === fn);
    if (index > -1) {
      listeners.splice(index, 1);
    }
  };

  const onKeyUp = (fn: Function) => {
    return subscribe(KEY_UP, fn);
  };

  const keyup = (event: any) => {
    const { key } = event;
    handle(KEY_UP, key);
  };

  keyListener.addEventListener('keyup', keyup);

  const onKeyDown = (fn: Function) => {
    return subscribe(KEY_DOWN, fn);
  };

  const keydown = (event: any) => {
    const { key } = event;
    handle(KEY_DOWN, key);
  };

  keyListener.addEventListener('keydown', keydown);

  const onMouseWheel = (fn: Function) => {
    return subscribe(MOUSE_WHEEL, fn);
  };

  const mouseWheel = (event: any) => {
    const delta = Math.sign(event.deltaY);
    handle(MOUSE_WHEEL, delta);
  };

  dom.addEventListener('wheel', mouseWheel);

  const onMouseMove = (fn: Function) => {
    return subscribe(MOUSE_MOVE, fn);
  };

  const mouseMove = (event: any) => {
    const boundingBox = event.target.getBoundingClientRect();
    const rawX = event.clientX - boundingBox.x;
    const rawY = event.clientY - Math.ceil(boundingBox.y);
    const mouse = {
      rawX,
      rawY,
      x: (rawX / (dom.innerWidth || Math.ceil(boundingBox.width))) * 2 - 1,
      y: -(rawY / (dom.innerHeight || Math.ceil(boundingBox.height))) * 2 + 1,
    };
    handle(MOUSE_MOVE, mouse);
  };

  dom.addEventListener('mousemove', mouseMove);

  const onMouseUp = (fn: Function) => {
    return subscribe(MOUSE_UP, fn);
  };

  const mouseUp = (event: any) => {
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
    handle(MOUSE_UP, mouse);
  };

  dom.addEventListener('mouseup', mouseUp);

  const contextMenu = (event: Event) => event.preventDefault();

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

  const handle = (eventType: string, event: any) => {
    const typeListeners = listeners[eventType];
    for (let i = 0; i < typeListeners.length; i++) {
      const listener = typeListeners[i];
      const handled = listener(event);
      if (handled) {
        return;
      }
    }
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

export interface InputHandler {
  onKeyUp: (fn: Function) => UnsubscribeCallback;
  onKeyDown: (fn: Function) => UnsubscribeCallback;
  onMouseWheel: (fn: Function) => UnsubscribeCallback;
  onMouseMove: (fn: Function) => UnsubscribeCallback;
  onMouseUp: (fn: Function) => UnsubscribeCallback;
  destroy: () => void;
}

export interface UnsubscribeCallback extends Function {}

interface ListenerContainer {
  [eventType: string]: Function[];
}
