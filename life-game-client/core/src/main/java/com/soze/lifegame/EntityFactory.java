package com.soze.lifegame;

import com.badlogic.gdx.assets.AssetManager;
import com.soze.klecs.engine.Engine;

public class EntityFactory {

  private final Engine engine;
  private final AssetManager assetManager;

  public EntityFactory(Engine engine, AssetManager assetManager) {
    this.engine = engine;
    this.assetManager = assetManager;
  }

}
