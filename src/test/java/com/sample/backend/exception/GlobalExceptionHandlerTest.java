package com.sample.backend.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  @Mock private WebRequest webRequest;

  @BeforeEach
  void setUp() {
    when(webRequest.getDescription(false)).thenReturn("uri=/api/movies/1");
  }

  @Test
  void handleEntityNotFoundException_ShouldReturnNotFound() {
    // Arrange
    EntityNotFoundException exception = new EntityNotFoundException("Movie not found with ID: 1");
    // Act
    ResponseEntity<ErrorResponse> response =
        GlobalExceptionHandler.handleEntityNotFoundException(exception, webRequest);
    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Not Found", response.getBody().getError());
    assertEquals("Movie not found with ID: 1", response.getBody().getMessage());
    assertEquals("uri=/api/movies/1", response.getBody().getPath());
    assertNotNull(response.getBody().getTimestamp());
  }

  @Test
  void handleGlobalException_ShouldReturnInternalServerError() {
    // Arrange
    Exception exception = new RuntimeException("Unexpected error");
    // Act
    ResponseEntity<ErrorResponse> response =
        GlobalExceptionHandler.handleGlobalException(exception, webRequest);
    // Assert
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Internal Server Error", response.getBody().getError());
    assertEquals("Unexpected error", response.getBody().getMessage());
    assertEquals("uri=/api/movies/1", response.getBody().getPath());
    assertNotNull(response.getBody().getTimestamp());
  }
}
