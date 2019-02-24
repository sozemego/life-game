package com.soze.lifegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.soze.klecs.engine.Engine;

public class DebugStage extends Stage {

  private final GameCamera gameCamera;
  private final Engine engine;

  private final Viewport stageViewport;
  private final Camera stageCamera;

  private final Label debugLabel;

  public DebugStage(GameCamera gameCamera, Engine engine) {
    this.gameCamera = gameCamera;
    this.engine = engine;

    stageCamera = new OrthographicCamera();
    stageViewport = new ScreenViewport(stageCamera);
    setViewport(stageViewport);

    Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
    debugLabel = new Label("", labelStyle);
    addActor(debugLabel);
  }

  @Override
  public void draw() {
    super.draw();

    int width = Gdx.graphics.getWidth();
    int height = Gdx.graphics.getHeight();

    debugLabel.setText(assembleDebugText());
    debugLabel.setPosition(width, height - 50);
  }

  public void resize(int width, int height) {
    stageViewport.update(width, height, true);
  }

  private String assembleDebugText()  {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("FPS: %s \n", Gdx.graphics.getFramesPerSecond()));
    sb.append(String.format("Camera middle: %s. Zoom %s \n", gameCamera.position, gameCamera.zoom));
    sb.append(String.format("Entities: %s \n", engine.getAllEntities().size()));
    return sb.toString();
  }

}
