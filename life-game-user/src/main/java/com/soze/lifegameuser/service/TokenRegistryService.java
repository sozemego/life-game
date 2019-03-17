package com.soze.lifegameuser.service;

import com.soze.lifegame.common.dto.user.TokenRegistration;
import com.soze.lifegameuser.exception.TokenRegistrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class TokenRegistryService {

  private static final Logger LOG = LoggerFactory.getLogger(TokenRegistryService.class);

  @Value("${life.game.server.token}")
  private String registerPath;


  private final RestTemplate restTemplate = new RestTemplate();

  public void registerToken(String username, String token) {
    LOG.info("Registering token for user [{}]", username);
    TokenRegistration tokenRegistration = new TokenRegistration(username, token);
    try {
      ResponseEntity<String> response = restTemplate.postForEntity(URI.create(registerPath),
                                                                   tokenRegistration, String.class);
      if (response.getStatusCode() != HttpStatus.CREATED) {
        throw new IllegalStateException("Could not register token!" + response);
      }
    } catch (Exception e) {
      throw new TokenRegistrationException(e);
    }

  }

}
