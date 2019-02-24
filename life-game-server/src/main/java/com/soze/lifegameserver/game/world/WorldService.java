package com.soze.lifegameserver.game.world;

import com.soze.lifegameserver.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    worldRepository.addWorld(world);
    return world;
  }
  
}
