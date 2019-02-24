package com.soze.lifegameuser.security;

import com.soze.lifegame.common.dto.user.LoginForm;
import com.soze.lifegame.common.json.JsonUtils;
import com.soze.lifegameuser.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final AuthService authService;

  @Autowired
  public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AuthService authService) {
    this.authenticationManager = authenticationManager;
    this.authService = authService;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    try {
      LoginForm form = JsonUtils.streamToObject(request.getInputStream(), LoginForm.class);

      return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          form.getUsername(),
          form.getPassword(),
          new ArrayList<>()
        )
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
