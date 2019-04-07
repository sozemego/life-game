package com.soze.lifegameserver.game.handler;

import com.soze.lifegameserver.game.GameCoordinator;
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
  private final GameCoordinator gameCoordinator;
  
  @Autowired
  public DisconnectHandler(SessionCache sessionCache, GameCoordinator gameCoordinator) {
    this.sessionCache = sessionCache;
    this.gameCoordinator = gameCoordinator;
  }
  
  @EventListener(condition = "#event.clientMessageType.equals('DISCONNECT')")
  public void handleDisconnect(ClientMessageEvent event) {
    LOG.info("Disconnecting session [{}]", event.getSession().getSession().getId());
    sessionCache.removeGameSession(event.getSession());
    gameCoordinator.removeGameEngine(event.getSession().getUserId());
  }
  
}
