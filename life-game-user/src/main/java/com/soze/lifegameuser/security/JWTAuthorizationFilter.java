package com.soze.lifegameuser.security;

import com.soze.lifegameuser.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

  private static final String AUTHORIZATION = "Authorization";
  private static final String AUTHENTICATION_SCHEME = "Bearer";

  private final AuthenticationManager authenticationManager;
  private final AuthService authService;

  @Autowired
  public JWTAuthorizationFilter(AuthenticationManager authenticationManager, AuthService authService) {
    super(authenticationManager);
    this.authenticationManager = authenticationManager;
    this.authService = authService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    String header = request.getHeader(AUTHORIZATION);

    if (header == null || !header.startsWith(AUTHENTICATION_SCHEME)) {
      chain.doFilter(request, response);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }


  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(AUTHORIZATION)
                     .substring(AUTHENTICATION_SCHEME.length())
                     .trim();

    if (!authService.validateToken(token)) {
      return null;
    }

    String username = authService.getUsernameClaim(token);
    return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
  }

}
