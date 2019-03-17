package com.soze.lifegameserver.game.engine.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.Objects;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type",
  visible = true
)
@JsonSubTypes(value = {
  @JsonSubTypes.Type(value = PhysicsComponent.class, name = "PHYSICS"),
  @JsonSubTypes.Type(value = GraphicsComponent.class, name = "GRAPHICS")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseComponent implements Serializable {
  
  private final ComponentType type;
  
  public BaseComponent(ComponentType type) {
    Objects.requireNonNull(this.type = type);
  }
  
  public ComponentType getType() {
    return type;
  }
  
  /**
   * A method which copies (clones) the component.
   * This is to implement a Template pattern for entities, so that they can be loaded
   * from a file/DB and then copied when they need to be added to the game.
   * This will stop us from having a parallel line of objects which describe how to convert
   * data from file to entity.
   */
  public abstract BaseComponent copy();
  
  public enum ComponentType {
    PHYSICS, GRAPHICS
  }
  
}
