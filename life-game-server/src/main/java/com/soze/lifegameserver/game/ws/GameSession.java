package com.soze.lifegameserver.game.ws;

import com.soze.lifegame.common.json.JsonUtils;
import com.soze.lifegameserver.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

public class GameSession {
  
  private static final Logger LOG = LoggerFactory.getLogger(GameSession.class);

  private final WebSocketSession session;
  private final User user;
  private final Instant createdAt;
  private long worldId;

  public GameSession(WebSocketSession session, User user, Instant createdAt) {
    this.session = Objects.requireNonNull(session);
    this.user = Objects.requireNonNull(user);
    this.createdAt = Objects.requireNonNull(createdAt);
  }

  public WebSocketSession getSession() {
    return session;
  }

  public User getUser() {
    return user;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
  
  public long getWorldId() {
    return worldId;
  }
  
  public void setWorldId(long worldId) {
    this.worldId = worldId;
  }
  
  public void send(Object object) {
    send(JsonUtils.objectToJson(object));
  }
  
  public void send(String body) {
    LOG.debug("{} sending message", getSession().getId());
    TextMessage textMessage = new TextMessage(body.getBytes());
    try {
      getSession().sendMessage(textMessage);
    } catch (IOException e) {
      e.printStackTrace();
      LOG.warn("Could not send message for game session {}", getSession().getId(), e);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GameSession that = (GameSession) o;
    return Objects.equals(user.getId(), that.user.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(user.getId());
  }
}
