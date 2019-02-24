package com.soze.lifegameuser.controller;

import com.soze.lifegame.common.api.ErrorResponse;
import com.soze.lifegame.common.dto.user.RegisterUserForm;
import com.soze.lifegame.common.dto.user.SimpleUserDto;
import com.soze.lifegameuser.entity.User;
import com.soze.lifegameuser.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserController {

  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  @Autowired
  public UserController(final UserService userService) {
    this.userService = Objects.requireNonNull(userService);
  }

  @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<SimpleUserDto>> allUsers() {
    LOG.info("GET request to /all");

    List<SimpleUserDto> dtos = userService.allUsers()
                                 .stream()
                                 .map(it -> new SimpleUserDto(it.getUsername()))
                                 .collect(Collectors.toList());

    return ResponseEntity.ok(dtos);
  }

  @PostMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SimpleUserDto> register(@RequestBody RegisterUserForm registerUserForm) {
    LOG.info("POST request to /register");
    userService.addUser(registerUserForm);
    return ResponseEntity.ok(new SimpleUserDto(registerUserForm.getUsername()));
  }

  @GetMapping(path = "/single/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getUserByUsername(@PathVariable("username") String username) {
    LOG.info("GET single user called [/single/{}]", username);
    Optional<User> userOptional = userService.getUserByUsername(username);

    if (!userOptional.isPresent()) {
      ErrorResponse errorResponse = new ErrorResponse(404, "User does not exist");
      errorResponse.addData("username", username);
      return ResponseEntity.status(404).body(errorResponse);
    }
    return ResponseEntity.ok(new SimpleUserDto(userOptional.get().getUsername()));
  }

  @DeleteMapping(path = "/single/delete")
  public ResponseEntity<?> deleteUser(Principal principal) {
    LOG.info("DELETE call to delete user [{}]", principal.getName());
    userService.deleteUser(principal.getName());
    return ResponseEntity.ok().build();
  }

  @GetMapping(path = "single/available/{username}")
  public ResponseEntity<Boolean> isUsernameAvailable(@PathVariable("username") String username) {
    LOG.info("GET call to check if username [{}] is available", username);
    boolean isAvailable = userService.isAvailableForRegistration(username);
    return ResponseEntity.ok(isAvailable);
  }

}
