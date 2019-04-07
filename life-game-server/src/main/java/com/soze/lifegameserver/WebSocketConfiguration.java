package com.soze.lifegameserver;

import com.soze.lifegameserver.game.interceptor.GameConnectionInterceptor;
import com.soze.lifegameserver.game.ws.GameHandler;
import com.soze.lifegameserver.statistics.StatisticsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

  @Autowired
  private GameHandler gameHandler;
  
  @Autowired
  private GameConnectionInterceptor gameConnectionInterceptor;
  
  @Autowired
  private StatisticsHandler statisticsHandler;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(gameHandler, "/game")
      .setAllowedOrigins("*")
      .addInterceptors(gameConnectionInterceptor)
    .addHandler(statisticsHandler, "/statistics")
      .setAllowedOrigins("*");
  }

}
