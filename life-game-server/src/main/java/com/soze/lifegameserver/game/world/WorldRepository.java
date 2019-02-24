package com.soze.lifegameserver.game.world;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Repository
@Transactional
public class WorldRepository {

  @PersistenceContext
  private EntityManager em;

  public Optional<World> findWorldByUserId(long userId) {
    return Optional.ofNullable(em.find(World.class, userId));
  }

  public World addWorld(World world) {
    em.persist(world);
    return world;
  }

  public void deleteWorld(World world) {
    world.setDeleted(true);
    world.setDeletedAt(new Timestamp(Instant.now().toEpochMilli()));
    em.merge(world);
  }

}
