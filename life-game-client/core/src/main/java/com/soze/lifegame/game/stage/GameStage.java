package com.soze.lifegame.game.stage;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.common.eventbus.Subscribe;
import com.soze.lifegame.ws.GameClient;
import com.soze.lifegame.ws.GameState;
import com.soze.lifegame.ws.event.GameStateChangedEvent;
import com.soze.r2d.R;
import com.soze.r2d.R2D;
import com.soze.r2d.UiState;

public class GameStage extends Stage {
  
  private final ScreenViewport stageViewport;
  private final Camera stageCamera;
  
  private final GameClient client;
  
  private final UiState uiState;
  
  public GameStage(GameClient client) {
    this.client = client;
    
    this.client.register(this);
  
    this.stageCamera = new OrthographicCamera();
    this.stageViewport = new ScreenViewport(stageCamera);
    setViewport(stageViewport);
    
    this.uiState = new UiState();
    uiState.set("dialogMessage",null);
  }
  
  public void resize(int width, int height) {
    R2D.render(R.createElement(GameUi.class), getRoot());
    getViewport().update(width, height, true);
  }
  
  public void update() {
    R2D.render(R.createElement(GameUi.class, uiState), getRoot());
  }
  
  @Subscribe
  public void handleGameStateChanged(GameStateChangedEvent e) {
    if (e.getGameState() == GameState.WORLD_REQUESTED) {
      setDialogMessage("REQUESTING WORLD DATA!");
    }
  }
  
  public void setDialogMessage(String message) {
    uiState.set("dialogMessage", message);
    update();
  }
  
}
