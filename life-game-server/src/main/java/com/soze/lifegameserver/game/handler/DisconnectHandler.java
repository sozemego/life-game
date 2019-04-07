package com.soze.lifegameserver.game.handler;

import com.soze.lifegameserver.game.SessionCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class DisconnectHandler {
  
  private static final Logger LOG = LoggerFactory.getLogger(DisconnectHandler.class);
  
  private final SessionCache sessionCache;
  
  @Autowired
  public DisconnectHandler(SessionCache sessionCache) {
    this.sessionCache = sessionCache;
  }
  
  @EventListener(condition = "#event.clientMessageType.equals('DISCONNECT')")
  public void handleDisconnect(ClientMessageEvent event) {
    LOG.info("Disocnnecting session [{}]", event.getSession().getSession().getId());
    sessionCache.removeGameSession(event.getSession());
  }
  
}
