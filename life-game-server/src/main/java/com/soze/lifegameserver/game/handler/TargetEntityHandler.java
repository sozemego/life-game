package com.soze.lifegameserver.game.handler;

import com.soze.lifegame.common.ws.message.client.TargetEntity;
import com.soze.lifegameserver.game.ws.GameSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Responsible for handling a message requesting one entity to target another.
 * The result of this depends on what source and target components.
 */
@Service
public class TargetEntityHandler {
  
  private static final Logger LOG = LoggerFactory.getLogger(TargetEntityHandler.class);
  
  @EventListener(condition = "#event.clientMessageType.equals('TARGET_ENTITY')")
  public void handleTargetEntity(ClientMessageEvent event) {
    handleTargetEntity(event.getSession(), (TargetEntity) event.getMessage());
  }
  
  private void handleTargetEntity(GameSession session, TargetEntity message) {
    LOG.info("User with session [{}] wants entity [{}] to target entity [{}]",
             session.getSession().getId(), message.getSourceEntityId(), message.getTargetEntityId()
    );
    
    
  }
  
}
