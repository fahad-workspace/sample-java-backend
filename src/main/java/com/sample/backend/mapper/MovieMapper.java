package com.sample.backend.mapper;

import com.sample.backend.dto.MovieDTO;
import com.sample.backend.dto.MovieDTO.MovieDTOBuilder;
import com.sample.backend.model.Movie;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for mapping between Movie entities and DTOs. Provides methods for converting Movie
 * objects to DTOs and back.
 */
@Slf4j
public final class MovieMapper {

  /** Converts a Movie entity to a MovieDTO. Includes director information if available. */
  public static MovieDTO toDTO(Movie movie) {
    if (movie == null) {
      log.debug("Attempted to convert null Movie to DTO");
      return null;
    }
    MovieDTOBuilder builder =
        MovieDTO.builder()
            .id(movie.getId())
            .title(movie.getTitle())
            .genre(movie.getGenre())
            .releaseDate(movie.getReleaseDate())
            .durationMinutes(movie.getDurationMinutes());
    if (movie.getDirector() != null) {
      builder
          .directorId(movie.getDirector().getId())
          .directorName(
              movie.getDirector().getFirstName() + " " + movie.getDirector().getLastName());
    }
    return builder.build();
  }

  /** Converts a MovieDTO to a Movie entity. Does not set director (must be handled separately). */
  public static Movie toEntity(MovieDTO dto) {
    if (dto == null) {
      log.debug("Attempted to convert null DTO to Movie");
      return null;
    }
    return Movie.builder()
        .title(dto.title())
        .genre(dto.genre())
        .releaseDate(dto.releaseDate())
        .durationMinutes(dto.durationMinutes())
        .build();
  }

  /**
   * Updates a Movie entity from a MovieDTO for partial updates. Only updates fields that are not
   * null in the DTO.
   */
  public static void updateMovieFromDTO(Movie movie, MovieDTO dto) {
    log.debug("Updating movie {} with DTO data", movie.getId());
    if (dto.title() != null) {
      movie.setTitle(dto.title());
    }
    if (dto.genre() != null) {
      movie.setGenre(dto.genre());
    }
    if (dto.releaseDate() != null) {
      movie.setReleaseDate(dto.releaseDate());
    }
    if (dto.durationMinutes() != null) {
      movie.setDurationMinutes(dto.durationMinutes());
    }
  }
}
