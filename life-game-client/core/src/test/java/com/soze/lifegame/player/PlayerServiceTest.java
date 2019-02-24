package com.soze.lifegame.player;

import com.soze.lifegame.api.ApiResponse;
import com.soze.lifegame.api.NetworkService;
import com.soze.lifegame.common.api.ErrorResponse;
import com.soze.lifegame.common.dto.user.LoginForm;
import com.soze.lifegame.common.dto.user.RegisterUserForm;
import com.soze.lifegame.common.dto.user.SimpleUserDto;
import com.soze.lifegame.exception.ApiException;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerServiceTest {

  private final Properties configuration = new Properties();
  private PlayerService playerService;

  @Before
  public void setup() {
    configuration.setProperty(PlayerApiCall.ALL_USERS.name(), PlayerApiCall.ALL_USERS.name());
    configuration.setProperty(PlayerApiCall.REGISTER_USER.name(), PlayerApiCall.REGISTER_USER.name());
    configuration.setProperty(PlayerApiCall.LOGIN.name(), PlayerApiCall.LOGIN.name());
  }

  @Test
  public void allUsers_oneUser() {
    String responseBody = "[{\"username\":\"hello\"}]";
    NetworkService networkService = mock(NetworkService.class);
    when(networkService.get(PlayerApiCall.ALL_USERS.name()))
      .thenReturn(
        new ApiResponse(HttpStatus.SC_OK, responseBody)
      );

    playerService = new PlayerService(networkService, configuration);
    List<SimpleUserDto> users = playerService.allUsers();

    assertEquals(1, users.size());
    assertEquals(users.get(0).getUsername(), "hello");
  }

  @Test
  public void allUsers_zeroUsers() {
    String responseBody = "[]";
    NetworkService networkService = mock(NetworkService.class);
    when(networkService.get(PlayerApiCall.ALL_USERS.name()))
      .thenReturn(
        new ApiResponse(HttpStatus.SC_OK, responseBody)
      );

    playerService = new PlayerService(networkService, configuration);
    List<SimpleUserDto> users = playerService.allUsers();

    assertEquals(0, users.size());
  }

  @Test(expected = ApiException.class)
  public void allUsers_error() {
    NetworkService networkService = mock(NetworkService.class);
    when(networkService.get(PlayerApiCall.ALL_USERS.name()))
      .thenThrow(
        new ApiException(new ErrorResponse(HttpStatus.SC_BAD_REQUEST, "Invalid parameter"))
      );

    playerService = new PlayerService(networkService, configuration);
    playerService.allUsers();
  }

  @Test
  public void register() {
    String responseBody = "{\"username\":\"username\"}";
    NetworkService networkService = mock(NetworkService.class);
    when(networkService.post(eq(PlayerApiCall.REGISTER_USER.name()), any(RegisterUserForm.class)))
      .thenReturn(new ApiResponse(HttpStatus.SC_CREATED, responseBody));

    playerService = new PlayerService(networkService, configuration);
    Player player = playerService.createPlayer("username", "password");
    assertEquals("username", player.getName());
  }

  @Test(expected = ApiException.class)
  public void register_userExists() {
    NetworkService networkService = mock(NetworkService.class);
    when(networkService.post(eq(PlayerApiCall.REGISTER_USER.name()), any(RegisterUserForm.class)))
      .thenThrow(new ApiException(new ErrorResponse(400, "username already exists")));

    new PlayerService(networkService, configuration).createPlayer("username", "password");
  }

  @Test(expected = ApiException.class)
  public void login_userDoesNotExist() {
    NetworkService networkService = mock(NetworkService.class);
    when(networkService.post(eq(PlayerApiCall.LOGIN.name()), any(LoginForm.class)))
      .thenThrow(new ApiException(new ErrorResponse(401, "Invalid username or password")));

    new PlayerService(networkService, configuration).login("username", "password");

  }

  @Test
  public void login_valid() {
    String responseBody = "{\"jwt\":\"tokentoken\"}";
    NetworkService networkService = mock(NetworkService.class);
    when(networkService.post(eq(PlayerApiCall.LOGIN.name()), any(LoginForm.class)))
      .thenReturn(new ApiResponse(HttpStatus.SC_OK, responseBody));
    playerService = new PlayerService(networkService, configuration);
    String token = playerService.login("username", "password");
    assertNotNull(token);
    assertTrue(token.equals("tokentoken"));
  }

}
