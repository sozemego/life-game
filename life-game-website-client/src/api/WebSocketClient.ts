export const createWebSocketClient = (options: any): WebSocketClient => {
  const listeners: MessageListeners = {};

  const path = options.path;
  if (!path) {
    throw new Error('options.path cannot be empty!');
  }

  let socket: WebSocket | null = null;

  const connect = () => {
    if (socket) {
      return Promise.reject('Already connected or connecting');
    }

    return new Promise((resolve, reject) => {
      socket = new WebSocket(path);

      socket.onopen = ws => {
        console.log('connected to game server');
        resolve();
      };

      socket.onmessage = message => {
        const parsed = JSON.parse(message.data);
        // console.log('received message from server');
        // console.log(parsed);
        // @ts-ignore
        const typeListeners = listeners[parsed.type] || [];
        typeListeners.forEach((listener) => listener(parsed));
      };

      socket.onclose = () => {
        console.log('connection closed');
      };

      socket.onerror = e => {
        console.log(e);
        reject(e);
      };
    });
  };

  const disconnect = () => {
    if (!socket) {
      throw new Error('Cannot disconnect, socket does not exist!');
    }
    console.log('disconnecting socket!');
    socket.close();
  };

  const send = (message: object) => {
    if (!socket) {
      throw new Error('Socket is not created');
    }
    socket.send(JSON.stringify(message));
  };

  const onMessage = (type: string, fn: (message: any) => void) => {
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

  return {
    connect,
    disconnect,
    send,
    onMessage,
  };
};

export interface WebSocketClient {
  connect: () => Promise<any>,
  disconnect: () => void;
  send: (message: object) => void;
  onMessage: (type: string, fn: (message: any) => void) => void;
}

export interface MessageListeners {
  [type: string]: Array<(data: any) => void>;
}
