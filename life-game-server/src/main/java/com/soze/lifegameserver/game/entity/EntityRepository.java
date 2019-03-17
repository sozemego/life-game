package com.soze.lifegameserver.game.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EntityRepository {
  
  private static final Logger LOG = LoggerFactory.getLogger(EntityRepository.class);
  
  private final Map<String, PersistentEntity> templates = new HashMap<>();
  
  @PersistenceContext
  private EntityManager em;
  
  @PostConstruct
  public void setup() {
    LOG.info("Loading entity templates");
    List<PersistentEntity> entityTemplates = em.createQuery("SELECT pe FROM PersistentEntity pe").getResultList();
    
    for (PersistentEntity template : entityTemplates) {
      LOG.info(template.toString());
      templates.put(template.getId(), template);
    }
    
    LOG.info("Loaded [{}] entity templates", templates.size());
  }
  
  public PersistentEntity getEntityTemplate(String id) {
    return templates.get(id).copy();
  }
  
}
