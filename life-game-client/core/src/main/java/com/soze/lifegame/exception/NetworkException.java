package com.soze.lifegame.exception;

import java.io.IOException;

public class NetworkException extends RuntimeException {

  public NetworkException(IOException e) {
    super(e);
  }
}
