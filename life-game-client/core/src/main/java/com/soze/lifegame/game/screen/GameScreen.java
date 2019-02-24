package com.soze.lifegame.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.soze.lifegame.LifeGame;
import com.soze.lifegame.ws.GameClient;
import com.soze.lifegame.ws.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameScreen implements Screen {
  
  private static final Logger LOG = LoggerFactory.getLogger(GameScreen.class);
  
  private final GameClient client;
  
  public GameScreen(LifeGame game) {
    this.client = game.getClient();
  }
  
  @Override
  public void show() {
    client.requestWorld();
  }
  
  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    
  }
  
  @Override
  public void resize(int width, int height) {
  
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
