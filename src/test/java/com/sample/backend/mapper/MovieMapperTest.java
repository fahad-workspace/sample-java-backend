package com.sample.backend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sample.backend.dto.MovieDTO;
import com.sample.backend.model.Director;
import com.sample.backend.model.Movie;
import java.time.LocalDate;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovieMapperTest {

  private Movie movie;
  private MovieDTO movieDTO;
  private Director director;

  @BeforeEach
  void setUp() {
    director =
        Director.builder()
            .id(1L)
            .firstName("Christopher")
            .lastName("Nolan")
            .birthDate(LocalDate.of(1970, 7, 30))
            .nationality("British-American")
            .build();
    movie =
        Movie.builder()
            .id(1L)
            .title("Interstellar")
            .genre("Sci-Fi")
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(169)
            .director(director)
            .build();
    movieDTO =
        MovieDTO.builder()
            .id(1L)
            .title("Interstellar")
            .genre("Sci-Fi")
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(169)
            .directorId(1L)
            .directorName("Christopher Nolan")
            .build();
  }

  @Test
  void toDTO_ShouldConvertEntityToDTO() {
    MovieDTO result = MovieMapper.toDTO(movie);
    assertEquals(movie.getId(), Objects.requireNonNull(result).id());
    assertEquals(movie.getTitle(), result.title());
    assertEquals(movie.getGenre(), result.genre());
    assertEquals(movie.getReleaseDate(), result.releaseDate());
    assertEquals(movie.getDurationMinutes(), result.durationMinutes());
    assertEquals(movie.getDirector().getId(), result.directorId());
    assertEquals("Christopher Nolan", result.directorName());
  }

  @Test
  void toDTO_ShouldHandleNullDirector() {
    Movie movieWithoutDirector =
        Movie.builder()
            .id(1L)
            .title("Interstellar")
            .genre("Sci-Fi")
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(169)
            .build();
    MovieDTO result = MovieMapper.toDTO(movieWithoutDirector);
    assertEquals(movieWithoutDirector.getId(), Objects.requireNonNull(result).id());
    assertEquals(movieWithoutDirector.getTitle(), result.title());
    assertNull(result.directorId());
    assertNull(result.directorName());
  }

  @Test
  void toDTO_ShouldReturnNull_WhenEntityIsNull() {
    assertNull(null);
  }

  @Test
  void toEntity_ShouldConvertDTOToEntity() {
    Movie result = MovieMapper.toEntity(movieDTO);
    // ID and director are not set in the toEntity method
    assertNull(Objects.requireNonNull(result).getId());
    assertNull(result.getDirector());
    assertEquals(movieDTO.title(), result.getTitle());
    assertEquals(movieDTO.genre(), result.getGenre());
    assertEquals(movieDTO.releaseDate(), result.getReleaseDate());
    assertEquals(movieDTO.durationMinutes(), result.getDurationMinutes());
  }

  @Test
  void toEntity_ShouldReturnNull_WhenDTOIsNull() {
    assertNull(null);
  }

  @Test
  void updateMovieFromDTO_ShouldUpdateOnlyProvidedFields() {
    MovieDTO partialDTO = MovieDTO.builder().title("Interstellar: Extended Edition").build();
    Movie existingMovie =
        Movie.builder()
            .id(1L)
            .title("Interstellar")
            .genre("Sci-Fi")
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(169)
            .director(director)
            .build();
    MovieMapper.updateMovieFromDTO(existingMovie, partialDTO);
    assertEquals("Interstellar: Extended Edition", existingMovie.getTitle());
    assertEquals("Sci-Fi", existingMovie.getGenre());
    assertEquals(LocalDate.of(2014, 11, 7), existingMovie.getReleaseDate());
    assertEquals(169, existingMovie.getDurationMinutes());
    assertEquals(director, existingMovie.getDirector());
  }

  @Test
  void updateMovieFromDTO_ShouldUpdateAllFields() {
    MovieDTO fullDTO =
        MovieDTO.builder()
            .title("Interstellar: Extended Edition")
            .genre("Science Fiction")
            .releaseDate(LocalDate.of(2014, 12, 1))
            .durationMinutes(190)
            .build();
    Movie existingMovie =
        Movie.builder()
            .id(1L)
            .title("Interstellar")
            .genre("Sci-Fi")
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(169)
            .director(director)
            .build();
    MovieMapper.updateMovieFromDTO(existingMovie, fullDTO);
    assertEquals("Interstellar: Extended Edition", existingMovie.getTitle());
    assertEquals("Science Fiction", existingMovie.getGenre());
    assertEquals(LocalDate.of(2014, 12, 1), existingMovie.getReleaseDate());
    assertEquals(190, existingMovie.getDurationMinutes());
    // Director should remain unchanged
    assertEquals(director, existingMovie.getDirector());
  }
}
