package com.soze.lifegameserver.game.ws;

import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import com.soze.lifegame.common.dto.world.EntityDto;
import com.soze.lifegame.common.dto.world.TileDto;
import com.soze.lifegame.common.dto.world.WorldDto;
import com.soze.lifegame.common.json.JsonUtils;
import com.soze.lifegame.common.ws.message.client.ClientMessage;
import com.soze.lifegame.common.ws.message.client.RequestWorldMessage;
import com.soze.lifegame.common.ws.message.server.EntityMessage;
import com.soze.lifegame.common.ws.message.server.WorldMessage;
import com.soze.lifegameserver.game.GameCoordinator;
import com.soze.lifegameserver.game.engine.GameEngine;
import com.soze.lifegameserver.game.entity.EntityService;
import com.soze.lifegameserver.game.entity.PersistentEntity;
import com.soze.lifegameserver.game.world.Tile;
import com.soze.lifegameserver.game.world.World;
import com.soze.lifegameserver.game.world.WorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class GameService {
  
  private static final Logger LOG = LoggerFactory.getLogger(GameService.class);
  
  private final WorldService worldService;
  private final GameCoordinator gameCoordinator;
  private final EntityService entityService;
  
  @Autowired
  public GameService(WorldService worldService, GameCoordinator gameCoordinator, EntityService entityService) {
    this.worldService = worldService;
    this.gameCoordinator = gameCoordinator;
    this.entityService = entityService;
  }
  
  @PostConstruct
  public void setup() {
    LOG.info("Loading all worlds");
    List<World> worlds = worldService.getAllWorlds();
    LOG.info("Retrieved [{}] worlds", worlds.size());
    for (World world : worlds) {
      addGameEngine(world);
    }
  }
  
  public void handleMessage(GameSession session, ClientMessage message) {
    if (message instanceof RequestWorldMessage) {
      handleRequestWorldMessage(session, (RequestWorldMessage) message);
    }
  }
  
  private void handleRequestWorldMessage(GameSession gameSession, RequestWorldMessage message) {
    LOG.info("{} requested the world", gameSession.getSession().getId());
    Optional<World> worldOptional = worldService.findWorldByUserId(gameSession.getUser().getId());
    boolean worldExists = worldOptional.isPresent();
    World world = worldExists ? worldOptional.get() : worldService.generateWorld(gameSession.getUser());
    WorldDto dto = convertToDto(world);
    
    GameEngine gameEngine = null;
    if (!worldExists) {
      LOG.info("World did not exist, adding to GameCoordinator");
      gameEngine = addGameEngine(world);
    } else {
      gameEngine = gameCoordinator.getGameEngineByUserId(world.getUserId());
    }
    
    LOG.info("Sending world data to {}", gameSession.getSession().getId());
    gameSession.send(new WorldMessage(UUID.randomUUID(), dto));
  
    Engine engine = gameEngine.getEngine();
    List<EntityDto> dtos = entityService.convert(engine);
    
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
  
  private GameEngine addGameEngine(World world) {
    GameEngine gameEngine = createGameEngine(world);
    gameCoordinator.addGameEngine(gameEngine);
    return gameEngine;
  }
  
  private GameEngine createGameEngine(World world) {
    Engine engine = new Engine();
    LOG.info("Adding [{}] entities to GameEngine for world with userId [{}]", world.getEntities().size(), world.getUserId());
    for (PersistentEntity persistentEntity : world.getEntities()) {
      Entity entity = entityService.convert(engine, persistentEntity);
      engine.addEntity(entity);
    }
    GameEngine gameEngine = new GameEngine(world, engine);
    return gameEngine;
  }
  
}
