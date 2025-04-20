package com.sample.backend.service;

import com.sample.backend.dto.MovieDTO;
import com.sample.backend.exception.EntityNotFoundException;
import com.sample.backend.mapper.MovieMapper;
import com.sample.backend.model.Director;
import com.sample.backend.model.Genre;
import com.sample.backend.model.Movie;
import com.sample.backend.model.Movie.MovieBuilder;
import com.sample.backend.repository.DirectorRepository;
import com.sample.backend.repository.MovieRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing movie-related operations. Handles business logic for creating, retrieving,
 * updating, and deleting movies.
 */
@Service
@Slf4j
public class MovieService extends BaseService<Movie, Long> {

  private final MovieRepository movieRepository;
  private final DirectorRepository directorRepository;

  public MovieService(MovieRepository movieRepository, DirectorRepository directorRepository) {
    super(movieRepository);
    this.movieRepository = movieRepository;
    this.directorRepository = directorRepository;
  }

  /**
   * Retrieves all movies.
   *
   * @return List of movie DTOs
   */
  public List<MovieDTO> getAllMovies() {
    log.debug("Fetching all movies");
    return movieRepository.findAll().stream().map(MovieMapper::toDTO).collect(Collectors.toList());
  }

  /**
   * Retrieves a movie by its ID.
   *
   * @param id Movie ID
   * @return Movie DTO
   * @throws EntityNotFoundException if movie not found
   */
  public MovieDTO getMovieById(Long id) {
    log.debug("Fetching movie with ID: {}", id);
    Movie movie = findById(id);
    return MovieMapper.toDTO(movie);
  }

  /**
   * Searches for movies by title (partial match).
   *
   * @param title Search term
   * @return List of matching movie DTOs
   */
  public List<MovieDTO> getMoviesByTitle(String title) {
    log.debug("Searching movies with title containing: {}", title);
    return movieRepository.findByTitleContainingIgnoreCase(title).stream()
        .map(MovieMapper::toDTO)
        .collect(Collectors.toList());
  }

  /**
   * Searches for movies by genre (exact match).
   *
   * @param genre Genre to search for
   * @return List of matching movie DTOs
   */
  public List<MovieDTO> getMoviesByGenre(Genre genre) {
    log.debug("Searching movies with genre: {}", genre);
    return movieRepository.findByGenre(genre).stream()
        .map(MovieMapper::toDTO)
        .collect(Collectors.toList());
  }

  public List<MovieDTO> getMoviesByTitleAndGenre(String title, Genre genre) {
    log.debug("Searching movies with title containing: {} and genre: {}", title, genre);
    return movieRepository.findByTitleContainingIgnoreCaseAndGenre(title, genre).stream()
        .map(MovieMapper::toDTO)
        .collect(Collectors.toList());
  }

  /**
   * Creates a new movie.
   *
   * @param movieDTO Movie data
   * @return Created movie DTO
   * @throws EntityNotFoundException if referenced director doesn't exist
   */
  @Transactional
  public MovieDTO createMovie(MovieDTO movieDTO) {
    log.info("Creating new movie: {}", movieDTO.title());
    // Build the movie entity
    MovieBuilder movieBuilder =
        Movie.builder()
            .title(movieDTO.title())
            .genre(movieDTO.genre())
            .releaseDate(movieDTO.releaseDate())
            .durationMinutes(movieDTO.durationMinutes());
    // Add director if provided
    if (movieDTO.directorId() != null) {
      log.debug("Fetching director with ID: {} for movie", movieDTO.directorId());
      Director director =
          directorRepository
              .findById(movieDTO.directorId())
              .orElseThrow(
                  () -> {
                    log.error("Director not found with ID: {}", movieDTO.directorId());
                    return new EntityNotFoundException(
                        "Director not found with ID: " + movieDTO.directorId());
                  });
      movieBuilder.director(director);
    }
    Movie savedMovie = movieRepository.save(movieBuilder.build());
    log.info("Movie created successfully with ID: {}", savedMovie.getId());
    return MovieMapper.toDTO(savedMovie);
  }

  /**
   * Updates an existing movie. Supports both full and partial updates.
   *
   * @param id Movie ID
   * @param movieDTO Updated movie data
   * @return Updated movie DTO
   * @throws EntityNotFoundException if movie or referenced director doesn't exist
   */
  @Transactional
  public MovieDTO updateMovie(Long id, MovieDTO movieDTO) {
    log.info("Updating movie with ID: {}", id);
    // Full replacement if all required fields are provided
    if ((movieDTO.title() != null)
        && (movieDTO.genre() != null)
        && (movieDTO.releaseDate() != null)
        && (movieDTO.durationMinutes() != null)) {
      log.debug("Performing full update of movie with ID: {}", id);
      // Verify movie exists
      findById(id);
      // Create new movie with builder
      MovieBuilder movieBuilder =
          Movie.builder()
              .id(id) // Keep the same ID
              .title(movieDTO.title())
              .genre(movieDTO.genre())
              .releaseDate(movieDTO.releaseDate())
              .durationMinutes(movieDTO.durationMinutes());
      // Set director if provided
      if (movieDTO.directorId() != null) {
        log.debug("Updating director to ID: {} for movie", movieDTO.directorId());
        Director director =
            directorRepository
                .findById(movieDTO.directorId())
                .orElseThrow(
                    () -> {
                      log.error("Director not found with ID: {}", movieDTO.directorId());
                      return new EntityNotFoundException(
                          "Director not found with ID: " + movieDTO.directorId());
                    });
        movieBuilder.director(director);
      }
      Movie updatedMovie = movieRepository.save(movieBuilder.build());
      log.info("Movie with ID: {} fully updated", id);
      return MovieMapper.toDTO(updatedMovie);
    }
    log.debug("Performing partial update of movie with ID: {}", id);
    Movie movie = findById(id);
    MovieMapper.updateMovieFromDTO(movie, movieDTO);
    // Update director reference if provided
    if (movieDTO.directorId() != null) {
      log.debug("Updating director to ID: {} for movie", movieDTO.directorId());
      Director director =
          directorRepository
              .findById(movieDTO.directorId())
              .orElseThrow(
                  () -> {
                    log.error("Director not found with ID: {}", movieDTO.directorId());
                    return new EntityNotFoundException(
                        "Director not found with ID: " + movieDTO.directorId());
                  });
      movie.setDirector(director);
    }
    Movie updatedMovie = movieRepository.save(movie);
    log.info("Movie with ID: {} partially updated", id);
    return MovieMapper.toDTO(updatedMovie);
  }

  /**
   * Deletes a movie by ID.
   *
   * @param id Movie ID
   * @throws EntityNotFoundException if movie doesn't exist
   */
  @Transactional
  public void deleteMovie(Long id) {
    log.info("Deleting movie with ID: {}", id);
    deleteById(id);
    log.info("Movie with ID: {} deleted successfully", id);
  }

  // Add to MovieService.java

  /**
   * Patches a movie with specified field updates.
   *
   * @param id Movie ID
   * @param updates Map of field names to their new values
   * @return Updated movie DTO
   * @throws EntityNotFoundException if movie doesn't exist
   */
  @Transactional
  public MovieDTO patchMovie(Long id, Map<String, Object> updates) {
    log.info("Patching movie with ID: {}", id);
    Movie movie = findById(id);
    // Apply updates
    updates.forEach(
        (key, value) -> {
          switch (key) {
            case "title":
              if (value != null) {
                movie.setTitle((String) value);
                log.debug("Updated movie title to: {}", value);
              }
              break;
            case "genre":
              if (value != null) {
                try {
                  movie.setGenre(Genre.valueOf(value.toString().toUpperCase()));
                  log.debug("Updated movie genre to: {}", value);
                } catch (IllegalArgumentException e) {
                  log.error("Invalid genre value: {}", value);
                  throw new IllegalArgumentException("Invalid genre: " + value);
                }
              }
              break;
            case "releaseDate":
              if (value != null) {
                movie.setReleaseDate(LocalDate.parse((String) value));
                log.debug("Updated movie release date to: {}", value);
              }
              break;
            case "durationMinutes":
              if (value != null) {
                if (value instanceof Integer) {
                  movie.setDurationMinutes((Integer) value);
                } else {
                  movie.setDurationMinutes(Integer.valueOf(value.toString()));
                }
                log.debug("Updated movie duration to: {}", value);
              }
              break;
            case "directorId":
              if (value != null) {
                long directorId;
                if (value instanceof Integer) {
                  directorId = ((Integer) value).longValue();
                } else {
                  directorId = Long.parseLong(value.toString());
                }
                log.debug("Updating director to ID: {} for movie", directorId);
                Director director =
                    directorRepository
                        .findById(directorId)
                        .orElseThrow(
                            () -> {
                              log.error("Director not found with ID: {}", directorId);
                              return new EntityNotFoundException(
                                  "Director not found with ID: " + directorId);
                            });
                movie.setDirector(director);
              }
              break;
            default:
              log.warn("Unknown field: {} with value: {} for movie patch", key, value);
              break;
          }
        });
    Movie updatedMovie = movieRepository.save(movie);
    log.info("Movie with ID: {} patched successfully", id);
    return MovieMapper.toDTO(updatedMovie);
  }
}
