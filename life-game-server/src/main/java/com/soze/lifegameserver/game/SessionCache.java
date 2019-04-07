package com.soze.lifegameserver.game;

import com.soze.lifegameserver.game.ws.GameSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores a map of userId -> GameSession.
 */
@Service
public class SessionCache {
  
  private static final Logger LOG = LoggerFactory.getLogger(SessionCache.class);
  
  private final Map<Long, GameSession> sessions = new ConcurrentHashMap<>();
  
  public void addGameSession(long userId, GameSession session) {
    LOG.info("Adding session for user id [{}] to cache", userId);
    sessions.put(userId, session);
  }
  
  public void removeGameSession(long worldId) {
    LOG.info("Removing session for world id [{}] from cache", worldId);
    sessions.remove(worldId);
  }
  
  public void removeGameSession(GameSession session) {
    removeGameSession(session.getUser().getId());
  }
  
  public Optional<GameSession> getSession(long userId) {
    return Optional.ofNullable(sessions.get(userId));
  }
  
}
