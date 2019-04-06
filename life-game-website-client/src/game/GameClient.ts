import { createWebSocketClient } from '../api/WebSocketClient';
import uuid from 'uuid/v4';
import { User } from '../user/dto';
import { EntityAction } from './game-engine/EntityAction';

export const createGameClient = (user: User): GameClient => {
  const webSocketClient = createWebSocketClient({
    path: 'ws://localhost:8000/game',
  });

  const authorize = () => {
    const authMessage = {
      username: user.name,
      token: user.token,
      messageId: uuid(),
      type: 'AUTHORIZE',
    };
    webSocketClient.send(authMessage);
  };

  const requestGameWorld = () => {
    const requestWorldMessage = {
      messageId: uuid(),
      type: 'REQUEST_WORLD',
    };
    webSocketClient.send(requestWorldMessage);
  };

  const targetEntity = (source: number, target: number, action: EntityAction) => {
    const targetEntityMessage = {
      messageId: uuid(),
      type: 'TARGET_ENTITY',
      sourceEntityId: source,
      targetEntityId: target,
      action,
    };
    webSocketClient.send(targetEntityMessage);
  };

  return {
    authorize,
    requestGameWorld,
    targetEntity,
    send: webSocketClient.send,
    connect: webSocketClient.connect,
    onMessage: webSocketClient.onMessage,
    disconnect: webSocketClient.disconnect,
  };
};

export interface GameClient {
  authorize: Function;
  requestGameWorld: Function;
  targetEntity: (source: number, target: number, action: EntityAction) => void;
  send: (data: object) => void;
  connect: Function;
  onMessage: (type: string, message: any) => void;
  disconnect: Function;
}
