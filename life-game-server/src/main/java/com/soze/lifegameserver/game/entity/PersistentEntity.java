package com.soze.lifegameserver.game.entity;

import com.soze.lifegameserver.game.engine.component.BaseComponent;
import com.soze.lifegameserver.game.engine.component.GraphicsComponent;
import com.soze.lifegameserver.game.engine.component.NameComponent;
import com.soze.lifegameserver.game.engine.component.PhysicsComponent;
import com.soze.lifegameserver.game.engine.component.ResourceProviderComponent;
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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entity")
@TypeDefs({
  @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class),
})
public class PersistentEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  
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
  
  @Type(type = "jsonb")
  @Column(name = "resource_provider", columnDefinition = "jsonb")
  private ResourceProviderComponent resourceProviderComponent;
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
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
  
  public ResourceProviderComponent getResourceProviderComponent() {
    return resourceProviderComponent;
  }
  
  public void setResourceProviderComponent(ResourceProviderComponent resourceProviderComponent) {
    this.resourceProviderComponent = resourceProviderComponent;
  }
  
  public PersistentEntity copy() {
    PersistentEntity copy = new PersistentEntity();
    copy.setPhysicsComponent(physicsComponent != null ? physicsComponent.copy() : null);
    copy.setGraphicsComponent(graphicsComponent != null ? graphicsComponent.copy() : null);
    copy.setName(getName());
    copy.setResourceProviderComponent(resourceProviderComponent != null ? resourceProviderComponent.copy() : null);
    return copy;
  }
  
  public List<BaseComponent> getAllComponents() {
    List<BaseComponent> components = new ArrayList<>();
    components.add(resourceProviderComponent);
    components.add(physicsComponent);
    components.add(graphicsComponent);
    components.add(new NameComponent(name));
    components.removeIf(c -> c == null);
    return components;
  }
  
  @Override
  public String toString() {
    return "PersistentEntity{" +
             "id=" + id +
             ", name='" + name + '\'' +
             ", worldId=" + worldId +
             ", physicsComponent=" + physicsComponent +
             ", graphicsComponent=" + graphicsComponent +
             ", resourceProviderComponent=" + resourceProviderComponent +
             '}';
  }
}
