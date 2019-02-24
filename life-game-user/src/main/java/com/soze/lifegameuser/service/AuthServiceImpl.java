package com.soze.lifegameuser.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.soze.lifegame.common.dto.user.ChangePasswordForm;
import com.soze.lifegame.common.dto.user.Jwt;
import com.soze.lifegame.common.dto.user.LoginForm;
import com.soze.lifegameuser.entity.User;
import com.soze.lifegameuser.exception.AuthUserDoesNotExistException;
import com.soze.lifegameuser.exception.IdenticalPasswordChangeException;
import com.soze.lifegameuser.exception.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

  private static final Logger LOG = LoggerFactory.getLogger(AuthServiceImpl.class);

  private static final String ISSUER = "life-game";
  private static final String USER_NAME_CLAIM = "username";
  private static final String USER_ID_CLAIM = "user_id";

  private final Algorithm algorithm;
  private final JwtKeyProvider keyProvider;
  private final UserService userService;
  private final PasswordHash passwordHash;

  @Autowired
  public AuthServiceImpl(JwtKeyProvider keyProvider, UserService userService, PasswordHash passwordHash) {
    this.algorithm = Algorithm.HMAC256(keyProvider.getSecret());
    this.keyProvider = keyProvider;
    this.userService = userService;
    this.passwordHash = passwordHash;
  }

  @Override
  public Jwt login(LoginForm loginForm) {
    LOG.info("Logging in user [{}]", loginForm.getUsername());
    User user = validateLogin(loginForm);
    return getToken(user);
  }

  @Override
  public Jwt getToken(User user) {
    return new Jwt(
      JWT.create()
        .withIssuer(ISSUER)
        .withClaim(USER_ID_CLAIM, user.getUserId())
        .withClaim(USER_NAME_CLAIM, user.getUsername())
        .sign(algorithm)
    );
  }

  private User validateLogin(LoginForm form) {
    User user = getUserByUsername(form.getUsername())
                  .orElseThrow(() -> {
                    form.reset();
                    return new AuthUserDoesNotExistException("Username: " + form.getUsername());
                  });

    boolean passwordMatches = passwordHash.matches(form.getPassword(), user.getPasswordHash());
    if (!passwordMatches) {
      form.reset();
      throw new InvalidPasswordException(form.getUsername());
    }

    form.reset();
    return user;
  }

  @Override
  public void logout(String token) {
    LOG.info("Logging out user");
  }

  @Override
  public boolean validateToken(String token) {
    Objects.requireNonNull(token);

    try {
      decodeToken(token);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  private DecodedJWT decodeToken(String token) {
    return JWT.require(algorithm)
             .build()
             .verify(token);
  }

  @Override
  public String getUsernameClaim(String token) {
    DecodedJWT decodedJWT = decodeToken(token);
    Claim claim = decodedJWT.getClaim(USER_NAME_CLAIM);
    if (claim.isNull()) {
      //TODO make own exception
      throw new IllegalArgumentException("NO USERNAME CLAIM");
    }
    return claim.asString();
  }

  @Override
  public void passwordChange(String username, ChangePasswordForm changePasswordForm) {
    LOG.info("Changing password for user [{}]", username);
    validatePasswordChange(username, changePasswordForm);

    userService.changeUserPassword(username, passwordHash.hashWithSalt(changePasswordForm.getNewPassword()));
    changePasswordForm.reset();
  }

  private void validatePasswordChange(String username, ChangePasswordForm form) {
    Objects.requireNonNull(username);
    Objects.requireNonNull(form);

    User user = getUserByUsername(username).orElseThrow(() -> {
      form.reset();
      return new AuthUserDoesNotExistException(username);
    });

    if (Arrays.equals(form.getNewPassword(), form.getOldPassword())) {
      form.reset();
      throw new IdenticalPasswordChangeException();
    }

    boolean passwordMatches = passwordHash.matches(form.getOldPassword(), user.getPasswordHash());
    if (!passwordMatches) {
      form.reset();
      throw new InvalidPasswordException(username);
    }
  }

  private Optional<User> getUserByUsername(String username) {
    return userService.getUserByUsername(username);
  }

}
