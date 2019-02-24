package com.soze.lifegame.game.stage;

import com.soze.r2d.Component;
import com.soze.r2d.Element;
import com.soze.r2d.R;
import com.soze.r2d.UiState;

import java.util.Arrays;

public class GameUi extends Component {
  
  public GameUi(UiState props) {
    super(props);
    UiState state = getState();
    state.set("dialogTitle", "LOADING");
  }
  
  private Element createMessageDialog() {
    UiState windowProps = new UiState();
    windowProps.set("title", getState().get("dialogTitle"));
    windowProps.set("fillParent", true);
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
