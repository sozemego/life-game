package com.soze.lifegameserver.tokenregistry.service;

import com.soze.lifegameserver.tokenregistry.dto.TokenRegistration;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class TokenRegistryServiceTest {

  private TokenRegistryService tokenRegistryService = new TokenRegistryService();

  @Before
  public void setup() {
    tokenRegistryService = new TokenRegistryService();
  }

  @Test
  public void testRegisterToken() {
    String username = "Username";
    tokenRegistryService.registerToken(new TokenRegistration(username, "Token"));
    assertTrue(tokenRegistryService.registeredTokens().containsKey(username));
  }

  @Test
  public void testMultipleRegisterToken() {
    String username = "Username";
    tokenRegistryService.registerToken(new TokenRegistration(username, "Token"));
    tokenRegistryService.registerToken(new TokenRegistration(username, "Token"));
    tokenRegistryService.registerToken(new TokenRegistration(username, "Token"));
    assertTrue(tokenRegistryService.registeredTokens().containsKey(username));
  }

  @Test
  public void testUnregisterToken() {
    String username = "Username";
    tokenRegistryService.registerToken(new TokenRegistration(username, "Token"));
    assertTrue(tokenRegistryService.registeredTokens().containsKey(username));
    tokenRegistryService.unregisterToken(username);
    assertFalse(tokenRegistryService.registeredTokens().containsKey(username));
  }

  @Test
  public void testUnregisterTokenFewTimes() {
    String username = "Username";
    tokenRegistryService.registerToken(new TokenRegistration(username, "Token"));
    assertTrue(tokenRegistryService.registeredTokens().containsKey(username));
    tokenRegistryService.unregisterToken(username);
    assertFalse(tokenRegistryService.registeredTokens().containsKey(username));
    tokenRegistryService.registerToken(new TokenRegistration(username, "Token"));
    assertTrue(tokenRegistryService.registeredTokens().containsKey(username));
    tokenRegistryService.unregisterToken(username);
    assertFalse(tokenRegistryService.registeredTokens().containsKey(username));
  }

  @Test
  public void testRegisterManyTimesThenUnregister() {
    String username = "Username";
    tokenRegistryService.registerToken(new TokenRegistration(username, "Token"));
    tokenRegistryService.registerToken(new TokenRegistration(username, "Token"));
    tokenRegistryService.registerToken(new TokenRegistration(username, "Token"));
    assertTrue(tokenRegistryService.registeredTokens().containsKey(username));
    tokenRegistryService.unregisterToken(username);
    assertFalse(tokenRegistryService.registeredTokens().containsKey(username));
  }

  @Test
  public void testIsTokenRegistered_valid() {
    String username = "Username";
    String token = "Token";
    tokenRegistryService.registerToken(new TokenRegistration(username, token));
    assertTrue(tokenRegistryService.isTokenRegistered(username, token));
  }

  @Test
  public void testIsTokenRegistered_notRegistered() {
    String username = "Username";
    String token = "Token";
    assertFalse(tokenRegistryService.isTokenRegistered(username, token));
  }

  @Test
  public void testIsTokenRegistered_invalidToken() {
    String username = "Username";
    String token = "Token";
    tokenRegistryService.registerToken(new TokenRegistration(username, "DIFFERENT TOKEN"));
    assertFalse(tokenRegistryService.isTokenRegistered(username, token));
  }

  @Test
  public void testIsTokenRegistered_differentUserHasIt() {
    String username = "Username";
    String token = "Token";
    tokenRegistryService.registerToken(new TokenRegistration("Different user", token));
    assertFalse(tokenRegistryService.isTokenRegistered(username, token));
  }

  @Test
  public void testIsTokenRegistered_afterUnregister() {
    String username = "Username";
    String token = "Token";
    tokenRegistryService.registerToken(new TokenRegistration(username, token));
    assertTrue(tokenRegistryService.isTokenRegistered(username, token));
    tokenRegistryService.unregisterToken(username);
    assertFalse(tokenRegistryService.isTokenRegistered(username, token));
  }

  @Test
  public void testIsTokenRegistered_fewRegistrations() {
    String username = "Username";
    String token = "Token";
    tokenRegistryService.registerToken(new TokenRegistration(username, token));
    assertTrue(tokenRegistryService.isTokenRegistered(username, token));
    tokenRegistryService.unregisterToken(username);
    assertFalse(tokenRegistryService.isTokenRegistered(username, token));
    tokenRegistryService.registerToken(new TokenRegistration(username, token));
    assertTrue(tokenRegistryService.isTokenRegistered(username, token));
    tokenRegistryService.unregisterToken(username);
    assertFalse(tokenRegistryService.isTokenRegistered(username, token));
  }

}
