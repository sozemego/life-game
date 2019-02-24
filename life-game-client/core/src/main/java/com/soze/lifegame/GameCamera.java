package com.soze.lifegame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import java.util.HashSet;
import java.util.Set;

public class GameCamera extends OrthographicCamera implements InputProcessor {

  public GameCamera() {

  }

  private final Set<Integer> pressedKeys = new HashSet<>();


  @Override
  public void update() {
    if (pressedKeys.contains(Input.Keys.A)) {
      this.position.x -= 5f;
    }
    if (pressedKeys.contains(Input.Keys.D)) {
      this.position.x += 5f;
    }
    if (pressedKeys.contains(Input.Keys.W)) {
      this.position.y += 5f;
    }
    if (pressedKeys.contains(Input.Keys.S)) {
      this.position.y -= 5f;
    }

    super.update();
  }

  @Override
  public boolean scrolled(int amount) {
    float zoomChange = amount > 0 ? 0.05f : -0.05f;
    zoom = MathUtils.clamp(zoom + zoomChange, 0.5f, 15f);
    return false;
  }

  @Override
  public boolean keyDown(int keycode) {
    return pressedKeys.add(keycode);
  }

  @Override
  public boolean keyUp(int keycode) {
    return pressedKeys.remove(keycode);
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return false;
  }
}
