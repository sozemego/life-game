package com.soze.lifegameserver.game.engine.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class HarvesterComponent extends BaseComponent {
  
  private Long targetId;
  
  /*
    Time in seconds for full harvest.
   */
  private float harvestingTime;
  
  private float harvestingProgress = 0f;
  
  public HarvesterComponent() {
    super(ComponentType.HARVESTER);
  }
  
  public HarvesterComponent(float harvestingTime, float harvestingProgress) {
    this();
    this.harvestingTime = harvestingTime;
    this.harvestingProgress = harvestingProgress;
  }
  
  public Long getTargetId() {
    return targetId;
  }
  
  public void setTargetId(Long targetId) {
    this.targetId = targetId;
  }
  
  public float getHarvestingTime() {
    return harvestingTime;
  }
  
  public void setHarvestingTime(float harvestingTime) {
    this.harvestingTime = harvestingTime;
  }
  
  public float getHarvestingProgress() {
    return harvestingProgress;
  }
  
  @JsonIgnore
  public boolean isHarvestComplete() {
    float threshold = 0.01f;
    return Math.abs(1f - harvestingProgress) < threshold;
  }
  
  public void setHarvestingProgress(float harvestingProgress) {
    this.harvestingProgress = harvestingProgress;
  }
  
  @Override
  public HarvesterComponent copy() {
    return new HarvesterComponent(getHarvestingTime(), getHarvestingProgress());
  }
}
