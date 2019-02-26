package com.soze.lifegameserver.game.ws;

import com.soze.lifegame.common.dto.world.TileDto;
import com.soze.lifegame.common.dto.world.WorldDto;
import com.soze.lifegame.common.json.JsonUtils;
import com.soze.lifegame.common.ws.message.client.ClientMessage;
import com.soze.lifegame.common.ws.message.client.RequestWorldMessage;
import com.soze.lifegame.common.ws.message.server.WorldMessage;
import com.soze.lifegameserver.game.world.Tile;
import com.soze.lifegameserver.game.world.World;
import com.soze.lifegameserver.game.world.WorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
    WorldDto dto = convertToDto(world);
    
    LOG.info("Sending world data to {}", gameSession.getSession().getId());
    gameSession.send(new WorldMessage(UUID.randomUUID(), dto));
  }
  
  private WorldDto convertToDto(World world) {
    LOG.info("Converting world with {} tiles to DTO", world.getTiles().size());
    Set<TileDto> tiles = new HashSet<>();
    for (Tile tile : world.getTiles()) {
      tiles.add(new TileDto(tile.getX(), tile.getY()));
    }
    return new WorldDto(world.getId(), world.getCreatedAt(), tiles);
  }
  
  
}
