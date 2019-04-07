package com.soze.lifegameserver.game.ws;

import com.auth0.jwt.JWT;
import com.soze.lifegame.common.json.JsonUtils;
import com.soze.lifegame.common.ws.message.client.AuthorizeMessage;
import com.soze.lifegame.common.ws.message.client.ClientMessage;
import com.soze.lifegame.common.ws.message.client.DisconnectMessage;
import com.soze.lifegame.common.ws.message.server.AuthorizedMessage;
import com.soze.lifegameserver.dto.User;
import com.soze.lifegameserver.game.SessionCache;
import com.soze.lifegameserver.game.handler.ClientMessageEvent;
import com.soze.lifegameserver.tokenregistry.service.TokenRegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameHandler extends TextWebSocketHandler {
  
  private static final Logger LOG = LoggerFactory.getLogger(GameHandler.class);
  
  private final TokenRegistryService tokenRegistryService;
  private final ApplicationEventPublisher applicationEventPublisher;
  
  private final Set<WebSocketSession> unauthorizedSessions = new HashSet<WebSocketSession>();
  private final Map<WebSocketSession, GameSession> authorizedSessions = new ConcurrentHashMap<>();
  
  @Autowired
  public GameHandler(TokenRegistryService tokenRegistryService,
                     ApplicationEventPublisher applicationEventPublisher) {
    this.tokenRegistryService = tokenRegistryService;
    this.applicationEventPublisher = applicationEventPublisher;
  }
  
  @Override
  //TODO write test for this method
  public void handleTextMessage(WebSocketSession session, TextMessage textMessage) {
    LOG.info("Message from [{}]", session.getId());
    //1. check for login message
    ClientMessage message = JsonUtils.jsonToObject(textMessage.getPayload(), ClientMessage.class);
    if (message instanceof AuthorizeMessage) {
      AuthorizeMessage authorizeMessage = (AuthorizeMessage) message;
      LOG.info("Received authorize message for user [{}]", authorizeMessage.getUsername());
      if (!tokenRegistryService.isTokenRegistered(authorizeMessage.getUsername(), authorizeMessage.getToken())) {
        LOG.info("User [{}] provided invalid token!", authorizeMessage.getUsername());
        closeSession(session, "Invalid token");
      } else {
        if (!authorizedSessions.containsKey(session)) {
          GameSession gameSession = createSession(session, authorizeMessage.getToken());
          LOG.info("Session [{}] authorized", gameSession);
          authorizedSessions.put(session, gameSession);
          gameSession.send(JsonUtils.objectToJson(new AuthorizedMessage(UUID.randomUUID())));
        } else {
          LOG.info("User [{}] already authorized, ignoring message", authorizeMessage.getUsername());
        }
      }
      unauthorizedSessions.remove(session);
    } else {
      //2. otherwise check if it's authorized already. If so, pass through
      GameSession existingSession = authorizedSessions.get(session);
      if (existingSession != null) {
        applicationEventPublisher.publishEvent(new ClientMessageEvent(existingSession, message));
      }
    }
  }
  
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    //TODO unauthorized sessions need to be cleared after a short time
    LOG.info("{} connected, adding to unauthorized sessions", session.getId());
    unauthorizedSessions.add(session);
  }
  
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    LOG.info("Session [{}] disconnected. Status [{}]", session.getId(), status);
    unauthorizedSessions.remove(session);
    GameSession gameSession = authorizedSessions.remove(session);
    if (gameSession != null) {
      applicationEventPublisher.publishEvent(new ClientMessageEvent(gameSession, new DisconnectMessage(UUID.randomUUID())));
    }
  }
  
  private GameSession createSession(WebSocketSession session, String token) {
    return new GameSession(session, new User(JWT.decode(token)), Instant.now());
  }
  
  private void closeSession(WebSocketSession session, String reason) {
    try {
      session.close(new CloseStatus(CloseStatus.NORMAL.getCode(), reason));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
