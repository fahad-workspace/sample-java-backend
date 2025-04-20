package com.sample.backend.controller;

import com.sample.backend.config.ApiStandardResponses;
import com.sample.backend.dto.MovieDTO;
import com.sample.backend.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for movie operations. Exposes endpoints for managing movies. */
@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movie", description = "Movie management APIs")
@Validated
@Slf4j
public class MovieController {

  private final MovieService movieService;

  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  /**
   * Get all movies.
   *
   * @return List of all movies
   */
  @Operation(summary = "Get all movies", description = "Retrieves a list of all available movies")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved all movies")
  @ApiStandardResponses
  @GetMapping
  public ResponseEntity<List<MovieDTO>> getAllMovies() {
    log.info("REST request to get all movies");
    return ResponseEntity.ok(movieService.getAllMovies());
  }

  /**
   * Get a specific movie by ID.
   *
   * @param id Movie ID
   * @return The requested movie
   */
  @Operation(summary = "Get movie by ID", description = "Retrieves a specific movie by its ID")
  @ApiResponse(responseCode = "200", description = "Movie found")
  @ApiStandardResponses
  @GetMapping("/{id}")
  public ResponseEntity<MovieDTO> getMovieById(
      @Parameter(description = "Movie ID", required = true) @PathVariable Long id) {
    log.info("REST request to get movie with ID: {}", id);
    return ResponseEntity.ok(movieService.getMovieById(id));
  }

  /**
   * Search movies by title or genre.
   *
   * @param title Optional title search term
   * @param genre Optional genre search term
   * @return List of matching movies
   */
  @Operation(summary = "Search movies", description = "Search movies by title or genre")
  @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
  @ApiStandardResponses
  @GetMapping("/search")
  public ResponseEntity<List<MovieDTO>> searchMovies(
      @Parameter(description = "Movie title (partial match)") @RequestParam(required = false)
          String title,
      @Parameter(description = "Movie genre (exact match)") @RequestParam(required = false)
          String genre) {
    if ((title != null) && !title.isEmpty()) {
      log.info("REST request to search movies by title: {}", title);
      return ResponseEntity.ok(movieService.getMoviesByTitle(title));
    }
    if ((genre != null) && !genre.isEmpty()) {
      log.info("REST request to search movies by genre: {}", genre);
      return ResponseEntity.ok(movieService.getMoviesByGenre(genre));
    }
    log.info("REST request to search movies with no parameters, returning all movies");
    return ResponseEntity.ok(movieService.getAllMovies());
  }

  /**
   * Create a new movie.
   *
   * @param movieDTO Movie data
   * @return Created movie
   */
  @Operation(summary = "Create movie", description = "Creates a new movie entry")
  @ApiResponse(
      responseCode = "201",
      description = "Movie created successfully",
      content = @Content(schema = @Schema(implementation = MovieDTO.class)))
  @ApiStandardResponses
  @PostMapping
  public ResponseEntity<MovieDTO> createMovie(
      @Parameter(description = "Movie data", required = true) @Valid @RequestBody
          MovieDTO movieDTO) {
    log.info("REST request to create movie: {}", movieDTO.title());
    return new ResponseEntity<>(movieService.createMovie(movieDTO), HttpStatus.CREATED);
  }

  /**
   * Update an existing movie.
   *
   * @param id Movie ID
   * @param movieDTO Updated movie data
   * @return Updated movie
   */
  @Operation(summary = "Update movie", description = "Updates an existing movie's information")
  @ApiResponse(responseCode = "200", description = "Movie updated successfully")
  @ApiStandardResponses
  @PutMapping("/{id}")
  public ResponseEntity<MovieDTO> updateMovie(
      @Parameter(description = "Movie ID", required = true) @PathVariable Long id,
      @Parameter(description = "Updated movie data", required = true) @Valid @RequestBody
          MovieDTO movieDTO) {
    log.info("REST request to update movie with ID: {}", id);
    return ResponseEntity.ok(movieService.updateMovie(id, movieDTO));
  }

  /**
   * Delete a movie.
   *
   * @param id Movie ID
   * @return No content response
   */
  @Operation(summary = "Delete movie", description = "Removes a movie from the database")
  @ApiResponse(responseCode = "204", description = "Movie deleted successfully")
  @ApiStandardResponses
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMovie(
      @Parameter(description = "Movie ID", required = true) @PathVariable Long id) {
    log.info("REST request to delete movie with ID: {}", id);
    movieService.deleteMovie(id);
    return ResponseEntity.noContent().build();
  }

  // Add to MovieController.java
  @Operation(
      summary = "Patch movie",
      description = "Partially updates an existing movie's information")
  @ApiResponse(responseCode = "200", description = "Movie patched successfully")
  @ApiStandardResponses
  @PatchMapping("/{id}")
  public ResponseEntity<MovieDTO> patchMovie(
      @Parameter(description = "Movie ID", required = true) @PathVariable Long id,
      @Parameter(description = "Fields to update", required = true) @RequestBody
          Map<String, Object> updates) {
    log.info("REST request to patch movie with ID: {}", id);
    return ResponseEntity.ok(movieService.patchMovie(id, updates));
  }
}
