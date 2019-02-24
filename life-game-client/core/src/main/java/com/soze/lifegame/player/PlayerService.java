package com.soze.lifegame.player;

import com.soze.lifegame.api.ApiResponse;
import com.soze.lifegame.api.ApiUtils;
import com.soze.lifegame.api.NetworkService;
import com.soze.lifegame.common.dto.user.Jwt;
import com.soze.lifegame.common.dto.user.LoginForm;
import com.soze.lifegame.common.dto.user.RegisterUserForm;
import com.soze.lifegame.common.dto.user.SimpleUserDto;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerService {

  private static final Logger LOG = LoggerFactory.getLogger(PlayerService.class);

  private final NetworkService networkService;
  private final Properties configuration;

  private final ExecutorService executorService = Executors.newFixedThreadPool(2);

  public PlayerService(NetworkService networkService, Properties configuration) {
    this.networkService = networkService;
    this.configuration = configuration;
  }

  public List<SimpleUserDto> allUsers() {
    String path = configuration.getProperty(PlayerApiCall.ALL_USERS.name());
    LOG.info("Retrieving a list of users from {}", path);
    ApiResponse response = networkService.get(path);
    List<SimpleUserDto> users = ApiUtils.readList(response, SimpleUserDto.class);
    LOG.info("Retrieved a list of {} users", users.size());
    return users;
  }

  public Player createPlayer(String name, String password) {
    String path = configuration.getProperty(PlayerApiCall.REGISTER_USER.name());
    LOG.info("Creating a player with name {} at endpoint {}", name, path);
    RegisterUserForm registerForm = new RegisterUserForm(name, password.toCharArray());
    ApiResponse response = networkService.post(path, registerForm);
    if (ApiUtils.getCode(response) == HttpStatus.SC_CREATED) {
      SimpleUserDto user = ApiUtils.readObject(response, SimpleUserDto.class);
      Player player = new Player(user.getUsername(), "");
      LOG.info("Created player with name {}", user.getUsername());
      return player;
    }
    throw new IllegalStateException("Something went wrong when creating the player " + response);
  }

  public CompletableFuture<Player> createPlayerAsync(String name, String password) {
    LOG.info("Calling createPlayerAsync");
    return CompletableFuture.supplyAsync(() -> createPlayer(name, password));
  }

  public String login(String name, String password) {
    LoginForm loginForm = new LoginForm(name, password.toCharArray());
    String path = configuration.getProperty(PlayerApiCall.LOGIN.name());
    LOG.info("Logging in player {} at endpoint {}", name, path);
    ApiResponse response = networkService.post(path, loginForm);
    if (ApiUtils.getCode(response) == HttpStatus.SC_OK) {
      Jwt token = ApiUtils.readObject(response, Jwt.class);
      LOG.info("Player {} logged in!", name);
      return token.getJwt();
    }
    throw new IllegalStateException("Something went wrong when logging in " + response);
  }

  public CompletableFuture<String> loginAsync(String name, String password) {
    LOG.info("Calling loginAsync");
    return CompletableFuture.supplyAsync(() -> login(name, password));
  }

}
