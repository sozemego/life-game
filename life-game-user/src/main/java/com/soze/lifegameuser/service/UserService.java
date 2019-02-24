package com.soze.lifegameuser.service;

import com.soze.lifegame.common.dto.user.RegisterUserForm;
import com.soze.lifegameuser.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

  @Deprecated()
  List<User> allUsers();


  Optional<User> getUserById(Long userId);

  Optional<User> getUserByUsername(String username);

  /**
   * Attempts to register a new user.
   * Username case is ignored.
   * Must clear password field from RegisterUserForm using the reset() method.
   * Username cannot be longer than 25 characters.
   */
  void addUser(RegisterUserForm userForm);

  void changeUserPassword(String username, String hash);

  /**
   * Attempts to delete a given user.
   * Deleted users cannot log in, but their names cannot be taken again either.
   */
  void deleteUser(String username);

  /**
   * Checks if given username is available for registration.
   * Returns true if it is available, false otherwise.
   * Also returns false for all illegal usernames.
   */
  boolean isAvailableForRegistration(String username);

}
