package com.soze.lifegame.ws.event;

import com.soze.lifegame.ws.GameState;

public class GameStateChangedEvent {
  
  private final GameState gameState;
  
  public GameStateChangedEvent(GameState gameState) {
    this.gameState = gameState;
  }
  
  public GameState getGameState() {
    return gameState;
  }
}
