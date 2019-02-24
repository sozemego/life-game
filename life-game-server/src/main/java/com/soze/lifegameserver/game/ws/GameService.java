package com.soze.lifegameserver.game.ws;

import com.soze.lifegame.common.ws.message.client.ClientMessage;
import com.soze.lifegame.common.ws.message.client.RequestWorldMessage;
import com.soze.lifegameserver.game.world.World;
import com.soze.lifegameserver.game.world.WorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService {
  
  private static final Logger LOG = LoggerFactory.getLogger(GameService.class);
  
  private final WorldService worldService;
  
  @Autowired
  public GameService(WorldService worldService) {
    this.worldService = worldService;
  }
  
  public void handleMessage(GameSession session, ClientMessage message) {
    if (message instanceof RequestWorldMessage) {
      handleRequestWorldMessage(session, (RequestWorldMessage) message);
    }
  }
  
  private void handleRequestWorldMessage(GameSession gameSession, RequestWorldMessage message) {
    LOG.info("{} requested the world", gameSession.getSession().getId());
    Optional<World> worldOptional = worldService.findWorldByUserId(gameSession.getUser().getId());
    
    World world = worldOptional.isPresent() ? worldOptional.get() : worldService.generateWorld(gameSession.getUser());
    
    LOG.info("Sending world data to {}", gameSession.getSession().getId());
  }
  
  
}
