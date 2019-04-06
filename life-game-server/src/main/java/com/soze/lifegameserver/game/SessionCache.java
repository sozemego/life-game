package com.soze.lifegameserver.game;

import com.soze.lifegameserver.game.ws.GameSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionCache {
  
  private static final Logger LOG = LoggerFactory.getLogger(SessionCache.class);
  
  private final Map<Long, GameSession> sessions = new ConcurrentHashMap<>();
  
  public void addGameSession(long worldId, GameSession session) {
    LOG.info("Adding session for world id [{}] to cache", worldId);
    sessions.put(worldId, session);
  }
  
  public void removeGameSession(long worldId) {
    LOG.info("Removing session for world id [{}] from cache", worldId);
    sessions.remove(worldId);
  }
  
  public void removeGameSession(GameSession session) {
    removeGameSession(session.getWorldId());
  }
  
  public Optional<GameSession> getSession(long worldId) {
    return Optional.ofNullable(sessions.get(worldId));
  }
  
}
