package com.soze.lifegame.game.stage;

import com.badlogic.gdx.Gdx;
import com.soze.r2d.Component;
import com.soze.r2d.Element;
import com.soze.r2d.R;
import com.soze.r2d.UiState;

import java.util.ArrayList;

public class MessageDialog extends Component {
  
  public MessageDialog(UiState props) {
    super(props);
  }
  
  @Override
  public Element render() {
    UiState dialogProps = new UiState();
//    dialogProps.set("title", getState().get("dialogTitle"));
    dialogProps.set("fillParent", getProps().get("fillParent", false));
    dialogProps.set("x", Gdx.graphics.getWidth() / 2f);
    dialogProps.set("y", Gdx.graphics.getHeight() / 2f);
    dialogProps.set("show", getProps().get("show"));
    dialogProps.set("debug", getProps().get("debug", false));
    
    return R.createElement("DIALOG", dialogProps, getProps().get("children", new ArrayList<>()));
  }
}
