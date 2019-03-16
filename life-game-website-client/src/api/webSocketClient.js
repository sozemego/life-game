export const createWebSocketClient = (options) => {

  const client = {};
  const listeners = {};

  const path = options.path;
  if (!path) {
    throw new Error('options.path cannot be empty!');
  }

  /**
   * @type {WebSocket}
   */
  let socket = null;

  client.connect = () => {
    if (socket) {
      return Promise.reject('Already connected or connecting');
    }

    socket = new WebSocket(path);

    return new Promise((resolve, reject) => {
      socket.onopen = (ws) => {
        console.log('connected to game server');
        resolve();
      };

      socket.onmessage = (message) => {
        const parsed = JSON.parse(message.data);
        console.log('received message from server');
        console.log(parsed);
        const typeListeners = listeners[parsed.type] || [];
        typeListeners.forEach(listener => listener(parsed));
      };

      socket.onclose = () => {
        console.log('connection closed');
      };

      socket.onerror = (e) => {
        console.log(e);
      };
    });
  };

  client.disconnect = () => {
    if (!socket) {
      throw new Error('Cannot disconnect, socket does not exist!');
    }
    console.log('disconnecting socket!');
    socket.close();
  };

  client.send = (message) => {
    socket.send(JSON.stringify(message));
  };

  client.onMessage = (type, fn) => {
    const typedListeners = listeners[type] || [];
    typedListeners.push(fn);
    listeners[type] = typedListeners;
    return () => {
      const index = typedListeners.findIndex(listener => listener === fn);
      if (index > -1) {
        typedListeners.splice(0, index);
      }
    };
  };

  return client;
};