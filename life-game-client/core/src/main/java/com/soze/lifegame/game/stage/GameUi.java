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
  }
  
  private Element createMessageDialog() {
    UiState dialogProps = new UiState();
    dialogProps.set("fillParent", true);
    dialogProps.set("show", getProps().get("dialogMessage") != null);
    dialogProps.set("debug", true);
  
    UiState labelProps = new UiState();
    labelProps.set("text", getProps().get("dialogMessage"));
    System.out.println(labelProps.get("text"));
    return R.createElement(MessageDialog.class, dialogProps, Arrays.asList(
      R.createElement("LABEL", labelProps)
    ));
  }
  
  @Override
  public Element render() {
    
    return createMessageDialog();
  }
}
