package com.soze.lifegameserver.game.world;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "world")
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
  
  @ElementCollection
  @CollectionTable(name = "tile", joinColumns = @JoinColumn(name = "world_id"))
  private Set<Tile> tiles;

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
}
