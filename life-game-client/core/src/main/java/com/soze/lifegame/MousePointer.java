package com.soze.lifegame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * A simple wrapper for methods related to a mouse pointer.
 *
 * @author soze
 */
public class MousePointer {

  /**
   * Returns Vector2 containing current mouse coordinates. X-axis is pointing right,
   * y-axis is pointing up.
   *
   * @return
   */
  public Vector2 mouseScreenCoordinates() {
    int x = Gdx.input.getX();
    int y = Gdx.graphics.getHeight() - Gdx.input.getY();
    return new Vector2(x, y);
  }

  public Vector2 rawMouseCoordinates() {
    int x = Gdx.input.getX();
    int y = Gdx.input.getY();
    return new Vector2(x, y);
  }

  /**
   * Translates mouse screen coordinates to be in the coordinates of
   * given camera.
   */
  public Vector2 getMouseUnprojectedCoordinates(Camera camera) {
    Vector2 screenCoordinates = rawMouseCoordinates();
    Vector3 unprojectedCoordinates = camera.unproject(new Vector3(screenCoordinates.x, screenCoordinates.y, 0f));
    return new Vector2(unprojectedCoordinates.x, unprojectedCoordinates.y);
  }

  /**
   * Translates mouse screen coordinates to be in the coordinates of
   * given viewport.
   */
  public Vector2 getMouseUnprojectedCoordinates(Viewport viewport) {
    return getMouseUnprojectedCoordinates(viewport.getCamera());
  }

}

