package com.soze.lifegame.utils;

import com.badlogic.gdx.utils.Timer;

/**
 * Utils related to {@link com.badlogic.gdx.utils.Timer}.
 */
public class TimerUtils {
  
  public static void post(Runnable runnable) {
    Timer.post(new Timer.Task() {
      @Override
      public void run() {
        runnable.run();
      }
    });
  }
  
}
