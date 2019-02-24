package com.soze.lifegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.google.common.eventbus.EventBus;
import com.soze.lifegame.game.screen.GameScreen;
import com.soze.lifegame.ws.GameClient;
import com.soze.r2d.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LifeGame extends Game {

  private static final Logger LOG = LoggerFactory.getLogger(LifeGame.class);
  
  private final EventBus eventBus = new EventBus();
  private final GameClient client = new GameClient(URI.create("http://localhost:8000/game"), eventBus);
  private AssetManager assetManager;
  
  @Override
  public void create() {
    LOG.info("Starting game!");
    
    AssetManager assetManager = new AssetManager();
  
    try {
      Files
        .walk(Paths.get("data/assets"))
        .map(it -> it.toFile())
        .filter(it -> it.getName().endsWith("png"))
        .peek(it -> LOG.info("Loading asset from {}", it))
        .forEach(it -> assetManager.load(it.getPath(), Texture.class));
    } catch (IOException e) {
      e.printStackTrace();
    }
  
    assetManager.finishLoading();
    LOG.info("Assets loaded");
  
    R.registerStateRunner((runnable) -> Timer.post(new Timer.Task() {
      @Override
      public void run() {
        runnable.run();
      }
    }));
    
    loadMainMenu();
  }
  
  public void loadMainMenu() {
    LOG.info("Loading main menu");
    setScreen(new MainMenuScreen(this, assetManager));
  }
  
  public void loadGameScreen() {
    LOG.info("Loading game screen");
    setScreen(new GameScreen(this));
  }
  
  public GameClient getClient() {
    return client;
  }
}
