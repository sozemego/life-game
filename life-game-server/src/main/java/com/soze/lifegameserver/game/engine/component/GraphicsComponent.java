package com.soze.lifegameserver.game.engine.component;

public class GraphicsComponent extends BaseComponent {
  
  private String texture;
  
  public GraphicsComponent() {
    super(ComponentType.GRAPHICS);
  }
  
  public GraphicsComponent(String texture) {
    this();
    this.texture = texture;
  }
  
  public String getTexture() {
    return texture;
  }
  
  public void setTexture(String texture) {
    this.texture = texture;
  }
  
  @Override
  public GraphicsComponent copy() {
    return new GraphicsComponent(texture);
  }
}
