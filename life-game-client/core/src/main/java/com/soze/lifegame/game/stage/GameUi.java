package com.soze.lifegame.game.stage;

import com.badlogic.gdx.Gdx;
import com.soze.r2d.Component;
import com.soze.r2d.Element;
import com.soze.r2d.R;
import com.soze.r2d.UiState;

public class GameUi extends Component {
  
  public GameUi(UiState props) {
    super(props);
    UiState state = getState();
    state.set("dialogTitle", "Status");
    state.set("text", "WHAT IS HAPPENING ARE YOU LOADING?");
  }
  
  private Element createMessageDialog() {
    UiState windowProps = new UiState();
    windowProps.set("title", getState().get("dialogTitle"));
    windowProps.set("fillParent", true);
    windowProps.set("text", getState().get("text"));
    windowProps.set("x", Gdx.graphics.getWidth() / 2f);
    windowProps.set("y", Gdx.graphics.getHeight() / 2f);
    return R.createElement("DIALOG", windowProps);
  }
  
  @Override
  public Element render() {
    
    return createMessageDialog();
//    return R.createElement("TABLE", new UiState(), Arrays.asList(
//      createMessageDialog()
//    ));
  }
}
