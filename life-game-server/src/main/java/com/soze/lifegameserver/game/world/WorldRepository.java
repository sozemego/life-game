package com.soze.lifegameserver.game.world;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class WorldRepository {

  @PersistenceContext
  private EntityManager em;

  public Optional<World> findWorldByUserId(long userId) {
    Query query = em.createQuery("SELECT w FROM World w WHERE w.userId = :userId");
    query.setParameter("userId", userId);
    
    try {
      return Optional.of((World) query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
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
  
  public List<World> getAllWorlds() {
    Query query = em.createQuery("SELECT w FROM World w");
    return query.getResultList();
  }
  
  public void updateWorld(World world) {
    em.merge(world);
  }
}
