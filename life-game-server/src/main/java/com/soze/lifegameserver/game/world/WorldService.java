package com.soze.lifegameserver.game.world;

import com.soze.lifegameserver.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class WorldService {
  
  private static final Logger LOG = LoggerFactory.getLogger(WorldService.class);

  private final WorldRepository worldRepository;

  @Autowired
  public WorldService(WorldRepository worldRepository) {
    this.worldRepository = worldRepository;
  }

  public Optional<World> findWorldByUserId(long userId) {
    return worldRepository.findWorldByUserId(userId);
  }
  
  public World generateWorld(User user) {
    LOG.info("Generating world for user {}", user.getName());
    World world = new World();
    world.setUserId(user.getId());
    world.setCreatedAt(new Timestamp(Instant.now().toEpochMilli()));
    generateTiles(world);
    worldRepository.addWorld(world);
    return world;
  }
  
  private void generateTiles(World world) {
    Set<Tile> tiles = new HashSet<>();
    for (int i = 0; i < 50; i++) {
      for (int j = 0; j < 50; j++) {
        Tile tile = new Tile();
        tile.setX(i);
        tile.setY(j);
        tiles.add(tile);
      }
    }
    LOG.info("Generated [{}] tiles for world belonging to user [{}]", tiles.size(), world.getUserId());
    world.setTiles(tiles);
  }
  
}
