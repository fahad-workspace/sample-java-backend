package com.sample.backend.controller;

import com.sample.backend.config.ApiStandardResponses;
import com.sample.backend.dto.DirectorDTO;
import com.sample.backend.service.DirectorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/directors")
@Tag(name = "Director", description = "Director management APIs")
@Validated
public class DirectorController {

  private final DirectorService directorService;

  public DirectorController(DirectorService directorService) {
    this.directorService = directorService;
  }

  @Operation(
      summary = "Get all directors",
      description = "Retrieves a list of all available directors")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved all directors")
  @ApiStandardResponses
  @GetMapping
  public ResponseEntity<List<DirectorDTO>> getAllDirectors() {
    return ResponseEntity.ok(directorService.getAllDirectors());
  }

  @Operation(
      summary = "Get director by ID",
      description = "Retrieves a specific director by its ID")
  @ApiResponse(responseCode = "200", description = "Director found")
  @ApiStandardResponses
  @GetMapping("/{id}")
  public ResponseEntity<DirectorDTO> getDirectorById(
      @Parameter(description = "Director ID", required = true) @PathVariable Long id) {
    return ResponseEntity.ok(directorService.getDirectorById(id));
  }

  @Operation(summary = "Search directors", description = "Search directors by name")
  @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
  @ApiStandardResponses
  @GetMapping("/search")
  public ResponseEntity<List<DirectorDTO>> searchDirectors(
      @Parameter(description = "Director name (partial match)") @RequestParam String name) {
    return ResponseEntity.ok(directorService.searchDirectors(name));
  }

  @Operation(summary = "Create director", description = "Creates a new director entry")
  @ApiResponse(
      responseCode = "201",
      description = "Director created successfully",
      content = @Content(schema = @Schema(implementation = DirectorDTO.class)))
  @ApiStandardResponses
  @PostMapping
  public ResponseEntity<DirectorDTO> createDirector(
      @Parameter(description = "Director data", required = true) @Valid @RequestBody
          DirectorDTO directorDTO) {
    return new ResponseEntity<>(directorService.createDirector(directorDTO), HttpStatus.CREATED);
  }

  @Operation(
      summary = "Update director",
      description = "Updates an existing director's information")
  @ApiResponse(responseCode = "200", description = "Director updated successfully")
  @ApiStandardResponses
  @PutMapping("/{id}")
  public ResponseEntity<DirectorDTO> updateDirector(
      @Parameter(description = "Director ID", required = true) @PathVariable Long id,
      @Parameter(description = "Updated director data", required = true) @Valid @RequestBody
          DirectorDTO directorDTO) {
    return ResponseEntity.ok(directorService.updateDirector(id, directorDTO));
  }

  @Operation(summary = "Delete director", description = "Removes a director from the database")
  @ApiResponse(responseCode = "204", description = "Director deleted successfully")
  @ApiStandardResponses
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDirector(
      @Parameter(description = "Director ID", required = true) @PathVariable Long id) {
    directorService.deleteDirector(id);
    return ResponseEntity.noContent().build();
  }
}
