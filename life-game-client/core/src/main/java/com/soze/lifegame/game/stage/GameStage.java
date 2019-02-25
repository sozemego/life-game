package com.soze.lifegame.game.stage;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.soze.lifegame.ws.GameClient;
import com.soze.r2d.R;
import com.soze.r2d.R2D;

public class GameStage extends Stage {
  
  private final ScreenViewport stageViewport;
  private final Camera stageCamera;
  
  private final GameClient client;
  
  public GameStage(GameClient client) {
    this.client = client;
    
    this.client.register(this);
  
    this.stageCamera = new OrthographicCamera();
    this.stageViewport = new ScreenViewport(stageCamera);
    setViewport(stageViewport);
  }
  
  public void resize(int width, int height) {
    R2D.render(R.createElement(GameUi.class), getRoot());
    getViewport().update(width, height, true);
  }
  
  public void update() {
    R2D.render(R.createElement(GameUi.class), getRoot());
  }
  
}
