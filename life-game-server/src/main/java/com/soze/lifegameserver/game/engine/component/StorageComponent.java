package com.soze.lifegameserver.game.engine.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soze.lifegameserver.game.world.Resource;

public class StorageComponent extends BaseComponent {
  
  private int capacity;
  
  private Resource resource;
  
  private int capacityTaken;
  
  public StorageComponent() {
    super(ComponentType.STORAGE);
  }
  
  public StorageComponent(int capacity, Resource resource, int capacityTaken) {
    this();
    this.capacity = capacity;
    this.resource = resource;
    this.capacityTaken = capacityTaken;
  }
  
  public int getCapacity() {
    return capacity;
  }
  
  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }
  
  public Resource getResource() {
    return resource;
  }
  
  public void setResource(Resource resource) {
    this.resource = resource;
  }
  
  public int getCapacityTaken() {
    return capacityTaken;
  }
  
  public void setCapacityTaken(int capacityTaken) {
    this.capacityTaken = capacityTaken;
  }
  
  @JsonIgnore
  public boolean isFull() {
    return capacity == capacityTaken;
  }
  
  @JsonIgnore
  public boolean hasFreeCapacity() {
    return capacityTaken < capacity;
  }
  
  @Override
  public StorageComponent copy() {
    return new StorageComponent(getCapacity(), getResource(), getCapacityTaken());
  }
}
