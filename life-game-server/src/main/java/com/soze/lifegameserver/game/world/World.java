package com.soze.lifegameserver.game.world;

import com.soze.lifegameserver.game.entity.PersistentEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "world")
@TypeDefs({
  @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class),
})
public class World {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;
  
  @Column(name = "user_id")
  private long userId;
  
  @Column(name = "created_at")
  private Timestamp createdAt;
  
  @Column(name = "deleted")
  private boolean deleted;
  
  @Column(name = "deleted_at")
  private Timestamp deletedAt;
  
  @Type(type = "jsonb")
  @Column(name = "tiles", columnDefinition = "jsonb")
  private Set<Tile> tiles;
  
  @OneToMany
  @JoinColumn(name = "world_id")
  private Set<PersistentEntity> entities;
  
  public long getId() {
    return id;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public long getUserId() {
    return userId;
  }
  
  public void setUserId(long userId) {
    this.userId = userId;
  }
  
  public Timestamp getCreatedAt() {
    return createdAt;
  }
  
  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }
  
  public boolean isDeleted() {
    return deleted;
  }
  
  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }
  
  public Timestamp getDeletedAt() {
    return deletedAt;
  }
  
  public void setDeletedAt(Timestamp deletedAt) {
    this.deletedAt = deletedAt;
  }
  
  public Set<Tile> getTiles() {
    return tiles;
  }
  
  public void setTiles(Set<Tile> tiles) {
    this.tiles = tiles;
  }
  
  public Set<PersistentEntity> getEntities() {
    return entities;
  }
  
  public void setEntities(Set<PersistentEntity> entities) {
    this.entities = entities;
  }
}
