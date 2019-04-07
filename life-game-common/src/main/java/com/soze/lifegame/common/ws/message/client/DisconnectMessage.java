package com.soze.lifegame.common.ws.message.client;

import java.util.UUID;

public class DisconnectMessage extends ClientMessage {
  
  public DisconnectMessage(UUID messageId) {
    super(messageId, ClientMessageType.DISCONNECT);
  }
}
