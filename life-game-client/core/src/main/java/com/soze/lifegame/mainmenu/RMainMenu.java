package com.soze.lifegame.mainmenu;


import com.soze.r2d.Component;
import com.soze.r2d.Element;
import com.soze.r2d.R;
import com.soze.r2d.UiState;

import java.util.ArrayList;
import java.util.Arrays;

public class RMainMenu extends Component {

  public RMainMenu(UiState props) {
    super(props);
  }

  private Element createLogo() {
    UiState labelProps = new UiState();
    labelProps.set("text", "Main menu");
    labelProps.set("row", true);

    return R.createElement("LABEL", labelProps, new ArrayList<>());
  }

  public Element render() {
    UiState tableProps = new UiState();
    tableProps.set("fillParent", true);

    UiState rLoginProps = new UiState();
    rLoginProps.set("row", true);
    rLoginProps.set("client", getProps().get("client"));

    return R.createElement("TABLE", tableProps, Arrays.asList(
      createLogo(),
      R.createElement(RLogin.class, rLoginProps, new ArrayList<>())
    ));
  }

}
