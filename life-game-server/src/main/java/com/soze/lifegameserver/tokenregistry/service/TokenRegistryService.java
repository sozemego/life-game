package com.soze.lifegameserver.tokenregistry.service;

import com.soze.lifegameserver.tokenregistry.dto.TokenRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenRegistryService {
  private static final Logger LOG = LoggerFactory.getLogger(TokenRegistryService.class);

  private Map<String, String> tokens = new ConcurrentHashMap();

  public void registerToken(TokenRegistration tokenRegistration) {
    LOG.info("Registering token for username {}", tokenRegistration.getUsername());
    tokens.put(tokenRegistration.getUsername(), tokenRegistration.getToken());
  }

  public void unregisterToken(String username) {
    LOG.info("Unregistering token for user [{}]", username);
    tokens.remove(username);
  }

  public Map<String, String> registeredTokens() {
    return tokens;
  }

  public boolean isTokenRegistered(String username, String token) {
    String existingToken = tokens.get(username);
    if (token.equals(existingToken)) {
      return true;
    }
    return false;
  }

}
