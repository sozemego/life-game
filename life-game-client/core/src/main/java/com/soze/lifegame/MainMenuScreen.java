package com.soze.lifegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.google.common.eventbus.Subscribe;
import com.soze.lifegame.ws.event.AuthorizedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainMenuScreen implements Screen {

  private static final Logger LOG = LoggerFactory.getLogger(MainMenuScreen.class);

  private final LifeGame lifeGame;
  private final AssetManager assetManager;
  private final MainMenuStage mainMenuStage;

  public MainMenuScreen(LifeGame lifeGame, AssetManager assetManager) {
    this.lifeGame = lifeGame;
    this.assetManager = assetManager;

    this.mainMenuStage = new MainMenuStage(assetManager, lifeGame.getClient());
    Gdx.input.setInputProcessor(mainMenuStage);

    lifeGame.getClient().register(this);
  }

  public void render(float delta) {
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    mainMenuStage.act();
    mainMenuStage.draw();
  }

  @Subscribe
  public void onAuthorized(AuthorizedEvent e) {
    LOG.info("Game server authorized client!");
    lifeGame.loadGameScreen();
  }

  public void resize(int width, int height) {
    mainMenuStage.resize(width, height);
  }

  @Override
  public void hide() {

  }

  @Override
  public void show() {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {

  }
}
