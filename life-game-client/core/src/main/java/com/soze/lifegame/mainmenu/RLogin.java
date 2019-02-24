package com.soze.lifegame.mainmenu;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.google.common.eventbus.Subscribe;
import com.soze.lifegame.api.DefaultNetworkService;
import com.soze.lifegame.config.ConfigLoader;
import com.soze.lifegame.exception.ApiException;
import com.soze.lifegame.player.Player;
import com.soze.lifegame.player.PlayerService;
import com.soze.lifegame.ws.GameClient;
import com.soze.lifegame.ws.event.AuthorizedEvent;
import com.soze.r2d.Component;
import com.soze.r2d.Element;
import com.soze.r2d.R;
import com.soze.r2d.UiState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RLogin extends Component {

  private final ConfigLoader configLoader = new ConfigLoader();
  private final PlayerService playerService = new PlayerService(
    new DefaultNetworkService(),
    configLoader.getConfig(ConfigLoader.Config.PLAYER_API)
  );

  private final GameClient client;

  public RLogin(UiState props) {
    super(props);
    UiState uiState = getState();
    uiState.set("username", "");
    uiState.set("password", "");
    uiState.set("message", "");
    uiState.set("processing", false);
    uiState.set("player", new Player("ANON", ""));
    this.client = Objects.requireNonNull((GameClient) props.get("client"));
    this.client.register(this);
  }

  @Subscribe
  private void onAuthorizedEvent(AuthorizedEvent e) {
    setState("processing", false);
    setState("message", "Connected to game server");
  }

  private Element usernameTextField() {
    UiState props = new UiState();
    props.set("text", getState().get("username"));
    props.set("row", true);
    props.set("onChange", (BiConsumer<TextField, Character>) this::usernameTextFieldChange);

    return R.createElement("TEXT_FIELD", props, new ArrayList<>());
  }

  private void usernameTextFieldChange(TextField field, char character) {
    setState("username", field.getText());
  }

  private Element passwordTextField() {
    UiState props = new UiState();
    props.set("text", getState().get("password"));
    props.set("row", true);
    props.set("onChange", (BiConsumer<TextField, Character>) this::passwordTextFieldChange);

    return R.createElement("TEXT_FIELD", props, new ArrayList<>());
  }

  private void passwordTextFieldChange(TextField field, char character) {
    setState("password", field.getText());
  }

  private Element messageLabel() {
    UiState props = new UiState();
    props.set("text", getState().get("message"));
    props.set("row", true);
    return R.createElement("LABEL", props, new ArrayList<>());
  }

  private Element registerButton() {
    String username = (String) getState().get("username");
    String password = (String) getState().get("password");
    boolean processing = (boolean) getState().get("processing");
    Player player = (Player) getState().get("player");

    UiState props = new UiState();
    props.set("text", "REGISTER");
    props.set("disabled", username.isEmpty() || password.isEmpty() || processing || !player.getToken().isEmpty());
    props.set("onClick", (Function<Event, Boolean>) this::onRegisterClick);

    return R.createElement("LABEL", props, new ArrayList<>());
  }

  private boolean onRegisterClick(Event event) {
    startProcessing();

    String username = (String) getState().get("username");
    String password = (String) getState().get("password");

    playerService
      .createPlayerAsync(username, password)
      .thenAccept(player -> {
        UiState nextState = new UiState();
        nextState.set("message", "Registered player!");
        nextState.set("player", player);
        setState(nextState);
      })
      .exceptionally(this::handleError)
      .thenRun(this::stopProcessing);

    return true;
  }

  private Element loginButton() {
    String username = (String) getState().get("username");
    String password = (String) getState().get("password");
    boolean processing = (boolean) getState().get("processing");

    UiState props = new UiState();
    props.set("text", "LOGIN");
    props.set("disabled", username.isEmpty() || password.isEmpty() || processing);
    props.set("onClick", (Function<Event, Boolean>) this::onLoginClick);

    return R.createElement("LABEL", props, new ArrayList<>());
  }

  private boolean onLoginClick(Event event) {
    startProcessing();

    String username = (String) getState().get("username");
    String password = (String) getState().get("password");

    playerService
      .loginAsync(username, password)
      .thenAccept(token -> {
        UiState nextState = new UiState();
        nextState.set("message", "Successfully logged in!");
        nextState.set("player", new Player(username, token));
        setState(nextState);
      })
      .thenRun(() -> {
        setState("message", "Connecting to game server.");
        client.login((Player) getState().get("player"));
      })
      .exceptionally(this::handleError);

    return true;
  }

  private <T> T handleError(Throwable e) {
    e.printStackTrace();
    if (e.getCause() instanceof ApiException) {
      ApiException apiException = (ApiException) e.getCause();
      setState("message", apiException.getError().getErrorMessage());
    } else {
      setState("message", "Network issue occured!");
    }
    return null;
  }
  
  private void startProcessing() {
    setState(
      new Object[] {"processing", true},
      new Object[] {"message", "Processing"}
    );
  }

  private void stopProcessing() {
    setState("processing", false);
  }

  public Element render() {
    UiState tableProps = new UiState();
    boolean row = getProps().get("row") == null ? false : (boolean) getProps().get("row");
    tableProps.set("row", row);

    UiState buttonTableProps = new UiState();
    buttonTableProps.set("row", true);

    return R.createElement("TABLE", tableProps, Arrays.asList(
      usernameTextField(),
      passwordTextField(),
      messageLabel(),
      R.createElement("TABLE", buttonTableProps, Arrays.asList(
        registerButton(),
        loginButton()
      ))
    ));
  }

}
