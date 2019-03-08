const uuid = require('uuid/v4');

export const createGameClient = (user) => {
  let socket = null;

  const client = {};
  const listeners = [];

  client.connect = () => {
    if (socket) {
      return Promise.reject('Already connected or connecting');
    }

    socket = new WebSocket('ws://localhost:8000/game?what=what');

    return new Promise((resolve, reject) => {
      socket.onopen = (ws) => {
        console.log('connected to game server');
        resolve();
      };

      socket.onmessage = (message) => {
        const parsed = JSON.parse(message.data);
        console.log('received message from server ' + parsed);
        listeners.forEach(listener => listener(parsed));
      };

      socket.onclose = () => {
        console.log('connection closed');
      };

      socket.onerror = (e) => {
        console.log(e);
      };
    });
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

  client.onMessage = (fn) => {
    listeners.push(fn);
    const index = listeners.length - 1;
    return () => listeners.splice(0, index);
  };

  return client;
};


