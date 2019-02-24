package com.soze.lifegameuser.service;

import com.soze.lifegame.common.dto.user.ChangePasswordForm;
import com.soze.lifegame.common.dto.user.Jwt;
import com.soze.lifegame.common.dto.user.LoginForm;
import com.soze.lifegameuser.entity.User;

public interface AuthService {

  /**
   * Attempts to authenticate the user and returns a token
   * used to identify the user.
   *
   * @param loginForm loginForm
   * @return jwt
   * @throws NullPointerException          if loginForm is null, or username in loginForm is null
   * @throws AuthUserDoesNotExistException if user with username specified in loginForm does not exist
   * @throws InvalidPasswordException      if password given by user is invalid
   */
  Jwt login(LoginForm loginForm);

  Jwt getToken(User user);

  /**
   * Given a token, logs out the user. For now, the method will only
   * log that user logged out.
   *
   * @param token jwt
   */
  void logout(String token);

  /**
   * Validates a given JWT. Returns true if it's valid, false otherwise.
   * This method does not check any claims, just checks whether the token
   * was tampered with.
   */
  boolean validateToken(String token);

  /**
   * Returns username claim from a given token.
   */
  String getUsernameClaim(String token);

  /**
   * Attempts to change user's password.
   * If the old password does not match, throws InvalidPasswordException
   *
   * @param changePasswordForm changePasswordForm
   * @throws NullPointerException     if changePasswordForm is null
   * @throws InvalidPasswordException if old password given does not match current password
   */
  void passwordChange(String username, ChangePasswordForm changePasswordForm);

}
