package com.sample.backend.controller;

import com.sample.backend.config.ApiStandardResponses;
import com.sample.backend.dto.ActorDTO;
import com.sample.backend.dto.PagedResponse;
import com.sample.backend.service.ActorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
@RequestMapping("/api/actors")
@Tag(name = "Actor", description = "Actor management APIs")
@Validated
public class ActorController {

  private final ActorService actorService;

  public ActorController(ActorService actorService) {
    this.actorService = actorService;
  }

  @Operation(
      summary = "Get all actors",
      description = "Retrieves a paginated list of all available actors")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved actors")
  @ApiStandardResponses
  @GetMapping
  public ResponseEntity<PagedResponse<ActorDTO>> getAllActors(
      @Parameter(description = "Page number (0-based)", example = "0")
          @RequestParam(defaultValue = "0")
          @Min(0)
          int page,
      @Parameter(description = "Page size", example = "10")
          @RequestParam(defaultValue = "10")
          @Min(1)
          int size,
      @Parameter(description = "Sort field", example = "lastName")
          @RequestParam(defaultValue = "id")
          String sort,
      @Parameter(description = "Sort direction (asc or desc)", example = "asc")
          @RequestParam(defaultValue = "asc")
          String direction) {
    return ResponseEntity.ok(actorService.getAllActors(page, size, sort, direction));
  }

  @Operation(summary = "Get actor by ID", description = "Retrieves a specific actor by its ID")
  @ApiResponse(responseCode = "200", description = "Actor found")
  @ApiStandardResponses
  @GetMapping("/{id}")
  public ResponseEntity<ActorDTO> getActorById(
      @Parameter(description = "Actor ID", required = true) @PathVariable Long id) {
    return ResponseEntity.ok(actorService.getActorById(id));
  }

  @Operation(summary = "Search actors", description = "Search actors by name")
  @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
  @ApiStandardResponses
  @GetMapping("/search")
  public ResponseEntity<PagedResponse<ActorDTO>> searchActors(
      @Parameter(description = "Actor name (partial match)") @RequestParam String name,
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "10") @Min(1) int size) {
    return ResponseEntity.ok(actorService.searchActors(name, page, size));
  }

  @Operation(summary = "Create actor", description = "Creates a new actor entry")
  @ApiResponse(
      responseCode = "201",
      description = "Actor created successfully",
      content = @Content(schema = @Schema(implementation = ActorDTO.class)))
  @ApiStandardResponses
  @PostMapping
  public ResponseEntity<ActorDTO> createActor(
      @Parameter(description = "Actor data", required = true) @Valid @RequestBody
          ActorDTO actorDTO) {
    return new ResponseEntity<>(actorService.createActor(actorDTO), HttpStatus.CREATED);
  }

  @Operation(summary = "Update actor", description = "Updates an existing actor's information")
  @ApiResponse(responseCode = "200", description = "Actor updated successfully")
  @ApiStandardResponses
  @PutMapping("/{id}")
  public ResponseEntity<ActorDTO> updateActor(
      @Parameter(description = "Actor ID", required = true) @PathVariable Long id,
      @Parameter(description = "Updated actor data", required = true) @Valid @RequestBody
          ActorDTO actorDTO) {
    return ResponseEntity.ok(actorService.updateActor(id, actorDTO));
  }

  @Operation(summary = "Delete actor", description = "Removes an actor from the database")
  @ApiResponse(responseCode = "204", description = "Actor deleted successfully")
  @ApiStandardResponses
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteActor(
      @Parameter(description = "Actor ID", required = true) @PathVariable Long id) {
    actorService.deleteActor(id);
    return ResponseEntity.noContent().build();
  }
}
