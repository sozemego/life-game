import {createWebSocketClient} from '../api/webSocketClient';
import uuid from 'uuid/v4';

/**
 * @return GameClient
 */
export const createGameClient = (user) => {

  const client = {};
  const webSocketClient = createWebSocketClient({path: 'ws://localhost:8000/game'});

  client.authorize = () => {
    const authMessage = {
      username: user.name,
      token: user.token,
      messageId: uuid(),
      type: 'AUTHORIZE'
    };
    webSocketClient.send(authMessage);
  };

  client.requestGameWorld = () => {
    const requestWorldMessage = {
      messageId: uuid(),
      type: 'REQUEST_WORLD'
    };
    webSocketClient.send(requestWorldMessage);
  };

  client.send = webSocketClient.send;
  client.connect = webSocketClient.connect;
  client.onMessage = webSocketClient.onMessage;

  return client;
};


