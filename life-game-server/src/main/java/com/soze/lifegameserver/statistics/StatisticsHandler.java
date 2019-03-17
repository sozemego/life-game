package com.soze.lifegameserver.statistics;

import com.soze.lifegame.common.dto.world.StatisticsWorldDto;
import com.soze.lifegame.common.json.JsonUtils;
import com.soze.lifegame.common.ws.message.server.StatisticsWorldMessage;
import com.soze.lifegameserver.game.GameCoordinator;
import com.soze.lifegameserver.game.GameRunner;
import com.soze.lifegameserver.game.engine.GameEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class StatisticsHandler extends TextWebSocketHandler {
  
  private static final Logger LOG = LoggerFactory.getLogger(StatisticsHandler.class);
  
  private final GameCoordinator gameCoordinator;
  private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
  
  @Autowired
  public StatisticsHandler(GameCoordinator gameCoordinator) {
    this.gameCoordinator = gameCoordinator;
  }
  
  @Scheduled(fixedRate = 10000)
  public void supplyClients() throws Exception {
    if (sessions.isEmpty()) {
      return;
    }
    LOG.debug("Supplying clients with statistics data");
    for (GameRunner gameRunner : gameCoordinator.getGameRunners()) {
      for (GameEngine engine : gameRunner.getEngines()) {
        StatisticsWorldDto dto = new StatisticsWorldDto(engine.getUserId(), engine.getCreatedAt());
        TextMessage textMessage = new TextMessage(JsonUtils.objectToJson(new StatisticsWorldMessage(UUID.randomUUID(),dto)));
        for (WebSocketSession session : sessions) {
          session.sendMessage(textMessage);
        }
      }
    }
  }
  
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    sessions.add(session);
  }
  
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    sessions.remove(session);
  }
}
