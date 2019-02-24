package com.soze.lifegameserver.tokenregistry.controller;

import com.soze.lifegameserver.tokenregistry.dto.TokenRegistration;
import com.soze.lifegameserver.tokenregistry.service.TokenRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.core.MediaType;

@Controller
@RequestMapping(path = "user")
public class UserController {

  @Autowired
  private TokenRegistryService tokenRegistryService;

  /**
   * Registers a token for given user. When the user tries to login to the game server,
   * they need to provide the exact same token.
   */
  @PostMapping(path = "token", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public ResponseEntity<?> registerToken(@RequestBody TokenRegistration registration) {
    tokenRegistryService.registerToken(registration);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}
