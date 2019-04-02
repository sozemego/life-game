import { createWebSocketClient } from "../api/WebSocketClient";

export const createStatisticsClient = () => {
  const client = {};
  const webSocketClient = createWebSocketClient({
    path: 'ws://localhost:8000/statistics',
  });

  client.connect = webSocketClient.connect;
  client.onMessage = webSocketClient.onMessage;
  client.disconnect = webSocketClient.disconnect;

  return client;
};
