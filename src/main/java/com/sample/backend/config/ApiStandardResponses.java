package com.sample.backend.config;

import com.sample.backend.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
  @ApiResponse(
      responseCode = "400",
      description = "Bad Request",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
  @ApiResponse(
      responseCode = "404",
      description = "Resource Not Found",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
  @ApiResponse(
      responseCode = "500",
      description = "Internal Server Error",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface ApiStandardResponses {
  // Reusable annotation for standard API responses
}
