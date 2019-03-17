package com.soze.lifegameserver.game.entity;

import com.soze.lifegameserver.game.engine.component.GraphicsComponent;
import com.soze.lifegameserver.game.engine.component.PhysicsComponent;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "entity")
@TypeDefs({
  @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class),
})
public class PersistentEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;
  
  @Column(name = "name")
  private String name;
  
  @Column(name = "world_id")
  private long worldId;
  
  @Type(type = "jsonb")
  @Column(name = "physics", columnDefinition = "jsonb")
  private PhysicsComponent physicsComponent;
  
  
  @Type(type = "jsonb")
  @Column(name = "graphics", columnDefinition = "jsonb")
  private GraphicsComponent graphicsComponent;
  
  public long getId() {
    return id;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public long getWorldId() {
    return worldId;
  }
  
  public void setWorldId(long worldId) {
    this.worldId = worldId;
  }
  
  public PhysicsComponent getPhysicsComponent() {
    return physicsComponent;
  }
  
  public void setPhysicsComponent(PhysicsComponent physicsComponent) {
    this.physicsComponent = physicsComponent;
  }
  
  public GraphicsComponent getGraphicsComponent() {
    return graphicsComponent;
  }
  
  public void setGraphicsComponent(GraphicsComponent graphicsComponent) {
    this.graphicsComponent = graphicsComponent;
  }
  
  public PersistentEntity copy() {
    PersistentEntity copy = new PersistentEntity();
    copy.setId(getId());
    copy.setPhysicsComponent(getPhysicsComponent());
    copy.setGraphicsComponent(getGraphicsComponent());
    return copy;
  }
  
  @Override
  public String toString() {
    return "PersistentEntity{" +
             "id=" + id +
             ", name='" + name + '\'' +
             ", worldId=" + worldId +
             ", physicsComponent=" + physicsComponent +
             ", graphicsComponent=" + graphicsComponent +
             '}';
  }
}
