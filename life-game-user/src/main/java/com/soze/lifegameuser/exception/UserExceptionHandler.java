package com.soze.lifegameuser.exception;

import com.soze.lifegame.common.api.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<?> handleInvalidPasswordException(InvalidPasswordException e) {
    int statusCode = HttpStatus.UNAUTHORIZED.value();
    ErrorResponse errorResponse = new ErrorResponse(statusCode, "Invalid username or password");
    return ExceptionUtils.convertErrorResponse(errorResponse);
  }

  @ExceptionHandler(UserRegistrationException.class)
  public ResponseEntity<?> handleUserRegistrationException(UserRegistrationException e) {
    int statusCode = HttpStatus.BAD_REQUEST.value();
    ErrorResponse errorResponse = new ErrorResponse(statusCode, e.getMessage());
    errorResponse.addData("field", e.getField());
    return ExceptionUtils.convertErrorResponse(errorResponse);
  }

  @ExceptionHandler(AuthUserDoesNotExistException.class)
  public ResponseEntity<?> handleAuthUserDoesNotExistException(AuthUserDoesNotExistException e) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password");
    return ExceptionUtils.convertErrorResponse(errorResponse);
  }

  @ExceptionHandler(NotAuthenticatedException.class)
  public ResponseEntity<?> handleNotAuthorizedException(NotAuthenticatedException e) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    return ExceptionUtils.convertErrorResponse(errorResponse);
  }

  @ExceptionHandler(TokenRegistrationException.class)
  public ResponseEntity<?> handleTokenRegistrationException(TokenRegistrationException e) {
    ErrorResponse errorResponse = new ErrorResponse(
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      "Cannot contact game server"
    );
    return ExceptionUtils.convertErrorResponse(errorResponse);
  }

}
