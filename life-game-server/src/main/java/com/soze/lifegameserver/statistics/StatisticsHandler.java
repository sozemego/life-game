package com.soze.lifegameserver.statistics;

import com.soze.lifegame.common.dto.user.SimpleUserDto;
import com.soze.lifegame.common.json.JsonUtils;
import com.soze.lifegameserver.game.GameCoordinator;
import com.soze.lifegameserver.game.GameRunner;
import com.soze.lifegameserver.game.engine.GameEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StatisticsHandler extends TextWebSocketHandler {
  
  private static final Logger LOG = LoggerFactory.getLogger(StatisticsHandler.class);
  
  @Value("${life.game.user.getById}")
  private String getByIdPath;
  
  private final GameCoordinator gameCoordinator;
  private final RestTemplate restTemplate = new RestTemplate();
  
  private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
  private final Map<Long, String> users = new ConcurrentHashMap<>();
  
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
    List<GameEngineDto> dtos = new ArrayList<>();
    for (GameRunner gameRunner : gameCoordinator.getGameRunners()) {
      for (GameEngine engine : gameRunner.getEngines()) {
        dtos.add(new GameEngineDto(getUsername(engine.getUserId()), engine.getCreatedAt()));
      }
    }
    
    TextMessage textMessage = new TextMessage(JsonUtils.objectToJson(new StatisticsData(dtos)));
    for (WebSocketSession session : sessions) {
      session.sendMessage(textMessage);
    }
  }
  
  private String getUsername(long userId) {
    return users.computeIfAbsent(userId, k -> {
      try {
        ResponseEntity<SimpleUserDto> response = restTemplate.getForEntity(URI.create(getByIdPath + "/" + k), SimpleUserDto.class);
        return response.getBody().getUsername();
      } catch (HttpClientErrorException e) {
        return String.valueOf(userId);
      }
    });
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
