package com.soze.lifegame.mainmenu;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.soze.lifegame.mainmenu.RMainMenu;
import com.soze.lifegame.ws.GameClient;
import com.soze.r2d.R;
import com.soze.r2d.R2D;
import com.soze.r2d.UiState;

import java.util.ArrayList;

public class MainMenuStage extends Stage {

  private final AssetManager assetManager;
  private final GameClient client;

  private final ScreenViewport stageViewport;
  private final Camera stageCamera;

  public MainMenuStage(AssetManager assetManager, GameClient client) {
    this.assetManager = assetManager;
    this.client = client;

    this.stageCamera = new OrthographicCamera();
    this.stageViewport = new ScreenViewport(stageCamera);
    setViewport(stageViewport);

    UiState rootState = new UiState();
    rootState.set("client", client);
    R2D.render(R.createElement(RMainMenu.class, rootState, new ArrayList<>()), getRoot());
    setDebugAll(true);
  }

  public void resize(int width, int height) {
    getViewport().update(width, height, true);
  }


}
