package com.soze.lifegame.ws;

import com.google.common.eventbus.EventBus;
import com.soze.lifegame.common.json.JsonUtils;
import com.soze.lifegame.common.ws.message.client.AuthorizeMessage;
import com.soze.lifegame.common.ws.message.server.AuthorizedMessage;
import com.soze.lifegame.common.ws.message.server.ServerMessage;
import com.soze.lifegame.player.Player;
import com.soze.lifegame.ws.event.AuthorizedEvent;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;


public class GameClient extends WebSocketClient {

  private static final Logger LOG = LoggerFactory.getLogger(GameClient.class);

  private final EventBus eventBus;

  public GameClient(URI serverUri, EventBus eventBus) {
    super(serverUri);
    this.eventBus = Objects.requireNonNull(eventBus);
  }

  public void register(Object object) {
    this.eventBus.register(object);
  }

  public void login(Player player) {
    if (!isOpen()) {
      LOG.info("Connecting to game client");
      try {
        connectBlocking();
      } catch (InterruptedException e) {
        e.printStackTrace();
        LOG.error(e.getMessage());
      }
    }
    if (!isOpen()) {
      LOG.warn("Client is not connected!");
      return;
    }

    LOG.info("Sending authorize message");
    send(JsonUtils.objectToJson(new AuthorizeMessage(UUID.randomUUID().toString(), player.getName(), player.getToken())));
  }

  @Override
  public void onOpen(ServerHandshake handshake) {
    LOG.info("Game client connected! {}", handshake.getHttpStatus());
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    LOG.info("Connection closed! Code [{}], reason [{}], remove [{}]", code, reason, remote);
  }

  @Override
  public void onMessage(String body) {
    ServerMessage message = JsonUtils.jsonToObject(body, ServerMessage.class);
    LOG.info("Received message! {}", message);
    if (message instanceof AuthorizedMessage) {
      eventBus.post(new AuthorizedEvent());
    }
  }

  @Override
  public void onError(Exception ex) {
    LOG.warn("Error in game client {}", ex);
  }

}
