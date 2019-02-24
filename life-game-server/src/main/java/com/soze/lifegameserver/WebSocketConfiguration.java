package com.soze.lifegameserver;

import com.soze.lifegameserver.game.interceptor.GameConnectionInterceptor;
import com.soze.lifegameserver.game.ws.AuthGameHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

  @Autowired
  private AuthGameHandler gameHandler;

  @Autowired
  private GameConnectionInterceptor gameConnectionInterceptor;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(gameHandler, "/game")
      .addInterceptors(gameConnectionInterceptor);
  }

}
