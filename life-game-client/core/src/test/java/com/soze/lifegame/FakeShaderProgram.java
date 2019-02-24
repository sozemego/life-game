package com.soze.lifegame;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class FakeShaderProgram extends ShaderProgram {

  public FakeShaderProgram(String vertexShader, String fragmentShader) {
    super(vertexShader, fragmentShader);
  }
}
