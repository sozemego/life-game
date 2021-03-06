package com.soze.lifegameserver.game.world;

import com.soze.lifegameserver.dto.User;
import com.soze.lifegameserver.game.engine.component.PhysicsComponent;
import com.soze.lifegameserver.game.entity.EntityService;
import com.soze.lifegameserver.game.entity.PersistentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class WorldService {
  
  private static final Logger LOG = LoggerFactory.getLogger(WorldService.class);

  private final WorldRepository worldRepository;
  private final EntityService entityService;

  @Autowired
  public WorldService(WorldRepository worldRepository, EntityService entityService) {
    this.worldRepository = worldRepository;
    this.entityService = entityService;
  }

  public Optional<World> findWorldByUserId(long userId) {
    LOG.info("Searching for world by user id [{}]", userId);
    Optional<World> worldByUserId = worldRepository.findWorldByUserId(userId);
    LOG.info("World for user [{}] exists [{}]", userId, worldByUserId.isPresent());
    return worldByUserId;
  }
  
  public List<World> getAllWorlds() {
    return worldRepository.getAllWorlds();
  }
  
  @Transactional
  public World generateWorld(User user) {
    LOG.info("Generating world for user {}", user.getName());
    World world = new World();
    world.setUserId(user.getId());
    world.setCreatedAt(new Timestamp(Instant.now().toEpochMilli()));
    world.setEntities(new HashSet<>());
    generateTiles(world);
    worldRepository.addWorld(world);
    addInitialEntities(world);
    return world;
  }
  
  private void generateTiles(World world) {
    Set<Tile> tiles = new HashSet<>();
    for (int i = 0; i < 250; i++) {
      for (int j = 0; j < 250; j++) {
        Tile tile = new Tile();
        tile.setX(i);
        tile.setY(j);
        tiles.add(tile);
      }
    }
    LOG.info("Generated [{}] tiles for world belonging to user [{}]", tiles.size(), world.getUserId());
    world.setTiles(tiles);
  }
  
  private void addInitialEntities(World world) {
    LOG.info("Adding initial entities to world with userId [{}]", world.getUserId());
    PersistentEntity warehouse = entityService.getTemplate("WAREHOUSE_1");
    warehouse.setId(null);
    warehouse.setWorldId(world.getId());
    warehouse.getGraphicsComponent().setTexture("WAREHOUSE_1");
    PhysicsComponent warehousePhysics = warehouse.getPhysicsComponent();
    warehousePhysics.setX(20);
    warehousePhysics.setY(25);
    world.getEntities().add(warehouse);
    
    LOG.info("Generating few forests for world with userId [{}]", world.getUserId());
    PersistentEntity forest = entityService.getTemplate("FOREST_1");
    forest.setId(null);
    forest.setWorldId(world.getId());
    PhysicsComponent forestPhysics = forest.getPhysicsComponent();
    forestPhysics.setX(27);
    forestPhysics.setY(26);
    world.getEntities().add(forest);
  
    PersistentEntity forest2 = entityService.getTemplate("FOREST_1");
    forest2.setId(null);
    forest2.setWorldId(world.getId());
    PhysicsComponent forest2Physics = forest2.getPhysicsComponent();
    forest2Physics.setX(30);
    forest2Physics.setY(26);
    world.getEntities().add(forest2);
  
    LOG.info("Generating workers for world with userId [{}]", world.getUserId());
    PersistentEntity worker1 = entityService.getTemplate("WORKER_1");
    worker1.setId(null);
    worker1.setWorldId(world.getId());
    PhysicsComponent worker1Physics = worker1.getPhysicsComponent();
    worker1Physics.setX(20);
    worker1Physics.setY(24);
    world.getEntities().add(worker1);
  }
  
}
