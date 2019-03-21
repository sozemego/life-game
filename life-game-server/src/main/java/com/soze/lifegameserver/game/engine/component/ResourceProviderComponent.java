package com.soze.lifegameserver.game.engine.component;

import com.soze.lifegameserver.game.world.Resource;

import java.util.Objects;

public class ResourceProviderComponent extends BaseComponent {
  
  private Resource resource;
  
  public ResourceProviderComponent() {
    super(ComponentType.RESOURCE_PROVIDER);
  }
  
  public ResourceProviderComponent(Resource resource) {
    this();
    this.resource = Objects.requireNonNull(resource);
  }
  
  public Resource getResource() {
    return resource;
  }
  
  public void setResource(Resource resource) {
    this.resource = resource;
  }
  
  @Override
  public ResourceProviderComponent copy() {
    return new ResourceProviderComponent(getResource());
  }
  
  @Override
  public String toString() {
    return "ResourceProviderComponent{" +
             "resource=" + resource +
             '}';
  }
}
