import { createLogger } from '../utils';

const uuid = require('uuid/v4');

const LOG = createLogger('client.js');

/**
 * @return GameClient
 */
export const createGameClient = (user) => {
  /**
   * @type {WebSocket}
   */
  let socket = null;

  const client = {};
  const listeners = {};

  client.connect = () => {
    if (socket) {
      return Promise.reject('Already connected or connecting');
    }

    socket = new WebSocket('ws://localhost:8000/game');

    return new Promise((resolve, reject) => {
      socket.onopen = (ws) => {
        LOG('connected to game server');
        resolve();
      };

      socket.onmessage = (message) => {
        const parsed = JSON.parse(message.data);
        LOG('received message from server');
        LOG(parsed);
        const typeListeners = listeners[parsed.type] || [];
        typeListeners.forEach(listener => listener(parsed));
      };

      socket.onclose = () => {
        LOG('connection closed');
      };

      socket.onerror = (e) => {
        LOG(e);
      };
    });
  };

  client.disconnect = () => {
    if (!socket) {
      throw new Error('Cannot disconnect, socket does not exist!');
    }
    LOG('disconnecting socket!');
    socket.close();
  };

  client.send = (message) => {
    socket.send(JSON.stringify(message));
  };

  client.authorize = () => {
    const authMessage = {
      username: user.name,
      token: user.token,
      messageId: uuid(),
      type: 'AUTHORIZE'
    };
    client.send(authMessage);
  };

  client.requestGameWorld = () => {
    const requestWorldMessage = {
      messageId: uuid(),
      type: 'REQUEST_WORLD'
    };
    client.send(requestWorldMessage);

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


