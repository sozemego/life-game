package com.soze.lifegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.soze.lifegame.LifeGame;
import com.soze.lifegame.game.stage.GameStage;
import com.soze.lifegame.game.stage.GameUi;
import com.soze.lifegame.ws.GameClient;
import com.soze.r2d.R;
import com.soze.r2d.R2D;
import com.soze.r2d.R2DNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameScreen implements Screen {
  
  private static final Logger LOG = LoggerFactory.getLogger(GameScreen.class);
  
  private final GameClient client;
  
  private final GameStage gameStage;
  
  public GameScreen(LifeGame game) {
    this.client = game.getClient();
    
    this.gameStage = new GameStage(this.client);
    
    Gdx.input.setInputProcessor(gameStage);
  }
  
  @Override
  public void show() {
    client.requestWorld();
    gameStage.update();
  }
  
  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    
    gameStage.act();
    gameStage.draw();
    
  }
  
  @Override
  public void resize(int width, int height) {
    gameStage.resize(width, height);
  }
  
  @Override
  public void pause() {
  
  }
  
  @Override
  public void resume() {
  
  }
  
  @Override
  public void hide() {
  
  }
  
  @Override
  public void dispose() {
  
  }
}
