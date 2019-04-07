package com.soze.lifegameserver.statistics;

import java.util.List;
import java.util.Objects;

public class StatisticsData {
  
  private final List<GameEngineDto> engines;
  
  public StatisticsData(List<GameEngineDto> engines) {
    this.engines = Objects.requireNonNull(engines);
  }
  
  public List<GameEngineDto> getEngines() {
    return engines;
  }
  
  public String getType() {
    return "STATISTICS_ENGINES";
  }
  
}
