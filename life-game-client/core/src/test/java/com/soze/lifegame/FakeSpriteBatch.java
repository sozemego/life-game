package com.soze.lifegame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class FakeSpriteBatch extends SpriteBatch {

  public FakeSpriteBatch(ShaderProgram shaderProgram) {
    super(5, shaderProgram);
  }

}
