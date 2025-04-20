package com.sample.backend.exception;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for the application. Provides centralized exception handling across all
 * controllers.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /** Handles EntityNotFoundException. Returns a 404 Not Found response. */
  @ExceptionHandler(EntityNotFoundException.class)
  public static ResponseEntity<ErrorResponse> handleEntityNotFoundException(
      EntityNotFoundException ex, WebRequest request) {
    log.error("Entity not found: {}", ex.getMessage());
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error("Not Found")
            .message(ex.getMessage())
            .path(request.getDescription(false))
            .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  /** Handles all other exceptions. Returns a 500 Internal Server Error response. */
  @ExceptionHandler(Exception.class)
  public static ResponseEntity<ErrorResponse> handleGlobalException(
      Exception ex, WebRequest request) {
    log.error("Unhandled exception: ", ex);
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("Internal Server Error")
            .message(ex.getMessage())
            .path(request.getDescription(false))
            .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
