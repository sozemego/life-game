package com.soze.lifegameserver.game.handler;

import com.soze.lifegame.common.ws.message.client.ClientMessage;
import com.soze.lifegameserver.game.ws.GameSession;

import java.util.Objects;

public class ClientMessageEvent {
  
  private final GameSession session;
  private final ClientMessage message;
  
  public ClientMessageEvent(GameSession session, ClientMessage message) {
    this.session = Objects.requireNonNull(session);
    this.message = Objects.requireNonNull(message);
  }
  
  public GameSession getSession() {
    return session;
  }
  
  public ClientMessage getMessage() {
    return message;
  }
  
  public String getClientMessageType() {
    return message.getType();
  }
  
}
