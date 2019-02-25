package com.soze.lifegame.game.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.soze.r2d.Component;
import com.soze.r2d.Element;
import com.soze.r2d.R;
import com.soze.r2d.UiState;

import java.util.Arrays;

public class GameUi extends Component {
  
  public GameUi(UiState props) {
    super(props);
    UiState state = getState();
    state.set("dialogTitle", "Status");
    state.set("text", "WHAT IS HAPPENING ARE YOU LOADING?");
    state.set("showDialog", true);
  }
  
  private Element createMessageDialog() {
    UiState dialogProps = new UiState();
//    dialogProps.set("title", getState().get("dialogTitle"));
    dialogProps.set("fillParent", true);
    dialogProps.set("x", Gdx.graphics.getWidth() / 2f);
    dialogProps.set("y", Gdx.graphics.getHeight() / 2f);
    dialogProps.set("show", getState().get("showDialog"));
    dialogProps.set("debug", true);
  
    UiState labelProps = new UiState();
    labelProps.set("text", getState().get("text"));
  
    return R.createElement(MessageDialog.class, dialogProps, Arrays.asList(
      R.createElement("LABEL", labelProps)
    ));
  }
  
  @Override
  public Element render() {
    
    return createMessageDialog();
//    return R.createElement("TABLE", new UiState(), Arrays.asList(
//      createMessageDialog()
//    ));
  }
}
