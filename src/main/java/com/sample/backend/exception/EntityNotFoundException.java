package com.sample.backend.exception;

import java.io.Serial;

public class EntityNotFoundException extends RuntimeException {

  @Serial private static final long serialVersionUID = 7023761642433782762L;

  public EntityNotFoundException(String message) {
    super(message);
  }
}
