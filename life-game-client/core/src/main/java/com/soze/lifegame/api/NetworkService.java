package com.soze.lifegame.api;

public interface NetworkService {

  ApiResponse get(String path);

  ApiResponse post(String path, String body);

  ApiResponse post(String path, Object body);

}
