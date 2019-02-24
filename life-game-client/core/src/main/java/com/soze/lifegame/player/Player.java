package com.soze.lifegame.player;

public class Player {

  private final String name;
  private final String token;

  public Player(String name, String token) {
    this.name = name;
    this.token = token;
  }

  public String getName() {
    return name;
  }

  public String getToken() {
    return token;
  }
}
