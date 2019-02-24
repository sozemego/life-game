package com.soze.lifegameuser.controller;

import com.soze.lifegame.common.dto.user.ChangePasswordForm;
import com.soze.lifegame.common.dto.user.Jwt;
import com.soze.lifegame.common.dto.user.LoginForm;
import com.soze.lifegameuser.service.AuthService;
import com.soze.lifegameuser.service.TokenRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping(path = "auth")
public class AuthController {

  private final AuthService authService;
  private final TokenRegistryService tokenRegistryService;

  @Autowired
  public AuthController(final AuthService authService, final TokenRegistryService tokenRegistryService) {
    this.authService = Objects.requireNonNull(authService);
    this.tokenRegistryService = Objects.requireNonNull(tokenRegistryService);
  }

  @PostMapping(path = "/login")
  public ResponseEntity<Jwt> login(@RequestBody LoginForm loginForm) {
    Jwt token = authService.login(loginForm);
    tokenRegistryService.registerToken(loginForm.getUsername(), token.getJwt());
    return ResponseEntity.ok(token);
  }

  @PostMapping(path = "/password/change")
  public ResponseEntity<?> passwordChange(@RequestBody ChangePasswordForm changePasswordForm, Principal principal) {
    authService.passwordChange(principal.getName(), changePasswordForm);
    return ResponseEntity.ok().build();
  }

  @PostMapping(path = "/logout")
  public ResponseEntity<?> logout() {
    return ResponseEntity.ok().build();
  }

}
