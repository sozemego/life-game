package com.soze.lifegameserver.game.handler;

import com.soze.lifegame.common.dto.world.EntityDto;
import com.soze.lifegame.common.dto.world.TileDto;
import com.soze.lifegame.common.dto.world.WorldDto;
import com.soze.lifegame.common.ws.message.client.RequestWorldMessage;
import com.soze.lifegame.common.ws.message.server.EntityMessage;
import com.soze.lifegame.common.ws.message.server.WorldMessage;
import com.soze.lifegameserver.game.GameCoordinator;
import com.soze.lifegameserver.game.SessionCache;
import com.soze.lifegameserver.game.engine.EngineFactory;
import com.soze.lifegameserver.game.engine.GameEngine;
import com.soze.lifegameserver.game.entity.EntityCache;
import com.soze.lifegameserver.game.entity.EntityService;
import com.soze.lifegameserver.game.world.Tile;
import com.soze.lifegameserver.game.world.World;
import com.soze.lifegameserver.game.world.WorldService;
import com.soze.lifegameserver.game.ws.GameSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class RequestWorldHandler {
  
  private static final Logger LOG = LoggerFactory.getLogger(RequestWorldHandler.class);
  
  private final WorldService worldService;
  private final GameCoordinator gameCoordinator;
  private final EntityService entityService;
  private final EntityCache entityCache;
  private final SessionCache sessionCache;
  private final EngineFactory engineFactory;
  
  @Autowired
  public RequestWorldHandler(WorldService worldService,
                             GameCoordinator gameCoordinator,
                             EntityService entityService,
                             EntityCache entityCache,
                             SessionCache sessionCache,
                             EngineFactory engineFactory) {
    this.worldService = worldService;
    this.gameCoordinator = gameCoordinator;
    this.entityService = entityService;
    this.entityCache = entityCache;
    this.sessionCache = sessionCache;
    this.engineFactory = engineFactory;
  }
  
  @EventListener(condition = "#event.clientMessageType.equals('REQUEST_WORLD')")
  public void handleRequestWorld(ClientMessageEvent event) {
    handleRequestWorldMessage(event.getSession(), (RequestWorldMessage) event.getMessage());
  }
  
  private void handleRequestWorldMessage(GameSession gameSession, RequestWorldMessage message) {
    LOG.info("{} requested the world", gameSession.getSession().getId());
    Optional<World> worldOptional = worldService.findWorldByUserId(gameSession.getUser().getId());
    boolean worldExists = worldOptional.isPresent();
    World world = worldExists ? worldOptional.get() : worldService.generateWorld(gameSession.getUser());
    WorldDto dto = convertToDto(world);
    
    if (!worldExists) {
      LOG.info("World did not exist, adding to GameCoordinator");
      addGameEngine(world);
    }
    
    gameSession.setWorldId(world.getId());
    sessionCache.addGameSession(world.getId(), gameSession);
    
    LOG.info("Sending world data to {}", gameSession.getSession().getId());
    gameSession.send(new WorldMessage(UUID.randomUUID(), dto));
    
    List<EntityDto> dtos = entityService.convert(entityCache.getEntities(world.getId()).values());
    
    LOG.info("Sending entity data about [{}] entities to [{}]", dtos.size(), gameSession.getSession().getId());
    gameSession.send(new EntityMessage(UUID.randomUUID(), dtos));
  }
  
  private WorldDto convertToDto(World world) {
    LOG.info("Converting world with {} tiles to DTO", world.getTiles().size());
    Set<TileDto> tiles = new HashSet<>();
    for (Tile tile : world.getTiles()) {
      tiles.add(new TileDto(tile.getX(), tile.getY()));
    }
    return new WorldDto(world.getId(), world.getCreatedAt(), tiles);
  }
  
  private void addGameEngine(World world) {
    GameEngine engine = engineFactory.createEngine(world);
    gameCoordinator.addGameEngine(engine);
  }
  
}
