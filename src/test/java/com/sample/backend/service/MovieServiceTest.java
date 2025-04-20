package com.sample.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sample.backend.dto.MovieDTO;
import com.sample.backend.exception.EntityNotFoundException;
import com.sample.backend.model.Director;
import com.sample.backend.model.Movie;
import com.sample.backend.repository.DirectorRepository;
import com.sample.backend.repository.MovieRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

  @Mock private MovieRepository movieRepository;
  @Mock private DirectorRepository directorRepository;
  @InjectMocks private MovieService movieService;
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
  void getAllMovies_ShouldReturnList() {
    when(movieRepository.findAll()).thenReturn(List.of(movie));
    List<MovieDTO> result = movieService.getAllMovies();
    assertEquals(1, result.size());
    assertEquals(movieDTO, result.getFirst());
  }

  @Test
  void getMovieById_ShouldReturnMovie_WhenMovieExists() {
    when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
    MovieDTO result = movieService.getMovieById(1L);
    assertEquals(movieDTO, result);
  }

  @Test
  void getMovieById_ShouldThrowException_WhenMovieDoesNotExist() {
    when(movieRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> movieService.getMovieById(99L));
  }

  @Test
  void getMoviesByTitle_ShouldReturnMatchingMovies() {
    when(movieRepository.findByTitleContainingIgnoreCase("Inter")).thenReturn(List.of(movie));
    List<MovieDTO> result = movieService.getMoviesByTitle("Inter");
    assertEquals(1, result.size());
    assertEquals(movieDTO, result.getFirst());
  }

  @Test
  void getMoviesByGenre_ShouldReturnMatchingMovies() {
    when(movieRepository.findByGenre("Sci-Fi")).thenReturn(List.of(movie));
    List<MovieDTO> result = movieService.getMoviesByGenre("Sci-Fi");
    assertEquals(1, result.size());
    assertEquals(movieDTO, result.getFirst());
  }

  @Test
  void createMovie_ShouldReturnCreatedMovie_WithoutDirector() {
    MovieDTO newMovieDTO =
        MovieDTO.builder()
            .title("Tenet")
            .genre("Action")
            .releaseDate(LocalDate.of(2020, 9, 3))
            .durationMinutes(150)
            .build();
    Movie savedMovie =
        Movie.builder()
            .id(2L)
            .title("Tenet")
            .genre("Action")
            .releaseDate(LocalDate.of(2020, 9, 3))
            .durationMinutes(150)
            .build();
    MovieDTO savedMovieDTO =
        MovieDTO.builder()
            .id(2L)
            .title("Tenet")
            .genre("Action")
            .releaseDate(LocalDate.of(2020, 9, 3))
            .durationMinutes(150)
            .build();
    when(movieRepository.save(any(Movie.class))).thenReturn(savedMovie);
    MovieDTO result = movieService.createMovie(newMovieDTO);
    assertEquals(savedMovieDTO, result);
  }

  @Test
  void createMovie_ShouldReturnCreatedMovie_WithDirector() {
    MovieDTO newMovieDTO =
        MovieDTO.builder()
            .title("Tenet")
            .genre("Action")
            .releaseDate(LocalDate.of(2020, 9, 3))
            .durationMinutes(150)
            .directorId(1L)
            .build();
    Movie savedMovie =
        Movie.builder()
            .id(2L)
            .title("Tenet")
            .genre("Action")
            .releaseDate(LocalDate.of(2020, 9, 3))
            .durationMinutes(150)
            .director(director)
            .build();
    MovieDTO savedMovieDTO =
        MovieDTO.builder()
            .id(2L)
            .title("Tenet")
            .genre("Action")
            .releaseDate(LocalDate.of(2020, 9, 3))
            .durationMinutes(150)
            .directorId(1L)
            .directorName("Christopher Nolan")
            .build();
    when(directorRepository.findById(1L)).thenReturn(Optional.of(director));
    when(movieRepository.save(any(Movie.class))).thenReturn(savedMovie);
    MovieDTO result = movieService.createMovie(newMovieDTO);
    assertEquals(savedMovieDTO, result);
  }

  @Test
  void createMovie_ShouldThrowException_WhenDirectorNotFound() {
    MovieDTO newMovieDTO =
        MovieDTO.builder()
            .title("Tenet")
            .genre("Action")
            .releaseDate(LocalDate.of(2020, 9, 3))
            .durationMinutes(150)
            .directorId(99L)
            .build();
    when(directorRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> movieService.createMovie(newMovieDTO));
  }

  @Test
  void updateMovie_FullUpdate_ShouldReturnUpdatedMovie() {
    MovieDTO fullUpdateDTO =
        MovieDTO.builder()
            .title("Tenet Updated")
            .genre("Sci-Fi Action")
            .releaseDate(LocalDate.of(2020, 9, 3))
            .durationMinutes(151)
            .directorId(1L)
            .build();
    Movie updatedMovie =
        Movie.builder()
            .id(1L)
            .title("Tenet Updated")
            .genre("Sci-Fi Action")
            .releaseDate(LocalDate.of(2020, 9, 3))
            .durationMinutes(151)
            .director(director)
            .build();
    MovieDTO updatedMovieDTO =
        MovieDTO.builder()
            .id(1L)
            .title("Tenet Updated")
            .genre("Sci-Fi Action")
            .releaseDate(LocalDate.of(2020, 9, 3))
            .durationMinutes(151)
            .directorId(1L)
            .directorName("Christopher Nolan")
            .build();
    when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
    when(directorRepository.findById(1L)).thenReturn(Optional.of(director));
    when(movieRepository.save(any(Movie.class))).thenReturn(updatedMovie);
    MovieDTO result = movieService.updateMovie(1L, fullUpdateDTO);
    assertEquals(updatedMovieDTO, result);
  }

  @Test
  void updateMovie_PartialUpdate_ShouldReturnPartiallyUpdatedMovie() {
    MovieDTO partialUpdateDTO = MovieDTO.builder().title("Interstellar: Extended Edition").build();
    Movie updatedMovie =
        Movie.builder()
            .id(1L)
            .title("Interstellar: Extended Edition")
            .genre("Sci-Fi")
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(169)
            .director(director)
            .build();
    MovieDTO updatedMovieDTO =
        MovieDTO.builder()
            .id(1L)
            .title("Interstellar: Extended Edition")
            .genre("Sci-Fi")
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(169)
            .directorId(1L)
            .directorName("Christopher Nolan")
            .build();
    when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
    when(movieRepository.save(any(Movie.class))).thenReturn(updatedMovie);
    MovieDTO result = movieService.updateMovie(1L, partialUpdateDTO);
    assertEquals(updatedMovieDTO, result);
  }

  @Test
  void updateMovie_WithDirector_ShouldUpdateDirectorReference() {
    MovieDTO partialUpdateDTO = MovieDTO.builder().directorId(1L).build();
    Movie updatedMovie =
        Movie.builder()
            .id(1L)
            .title("Interstellar")
            .genre("Sci-Fi")
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(169)
            .director(director)
            .build();
    MovieDTO updatedMovieDTO =
        MovieDTO.builder()
            .id(1L)
            .title("Interstellar")
            .genre("Sci-Fi")
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(169)
            .directorId(1L)
            .directorName("Christopher Nolan")
            .build();
    when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
    when(directorRepository.findById(1L)).thenReturn(Optional.of(director));
    when(movieRepository.save(any(Movie.class))).thenReturn(updatedMovie);
    MovieDTO result = movieService.updateMovie(1L, partialUpdateDTO);
    assertEquals(updatedMovieDTO, result);
  }

  @Test
  void deleteMovie_ShouldDeleteMovie_WhenMovieExists() {
    when(movieRepository.existsById(1L)).thenReturn(true);
    doNothing().when(movieRepository).deleteById(1L);
    movieService.deleteMovie(1L);
    verify(movieRepository).deleteById(1L);
  }

  @Test
  void deleteMovie_ShouldThrowException_WhenMovieDoesNotExist() {
    when(movieRepository.existsById(99L)).thenReturn(false);
    assertThrows(EntityNotFoundException.class, () -> movieService.deleteMovie(99L));
  }

  // Add to MovieServiceTest.java
  @Test
  void patchMovie_ShouldReturnPatchedMovie_WithMultipleFields() {
    // Arrange
    Map<String, Object> patches = new HashMap<>();
    patches.put("title", "Interstellar: Extended Cut");
    patches.put("durationMinutes", 195);
    Movie patchedMovie =
        Movie.builder()
            .id(1L)
            .title("Interstellar: Extended Cut")
            .genre("Sci-Fi")
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(195)
            .director(director)
            .build();
    MovieDTO patchedMovieDTO =
        MovieDTO.builder()
            .id(1L)
            .title("Interstellar: Extended Cut")
            .genre("Sci-Fi")
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(195)
            .directorId(1L)
            .directorName("Christopher Nolan")
            .build();
    when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
    when(movieRepository.save(any(Movie.class))).thenReturn(patchedMovie);
    // Act
    MovieDTO result = movieService.patchMovie(1L, patches);
    // Assert
    assertEquals(patchedMovieDTO, result);
  }

  @Test
  void patchMovie_ShouldUpdateDirectorReference_WhenDirectorIdPatched() {
    // Arrange
    Director newDirector =
        Director.builder()
            .id(2L)
            .firstName("Steven")
            .lastName("Spielberg")
            .birthDate(LocalDate.of(1946, 12, 18))
            .nationality("American")
            .build();
    Map<String, Object> patches = new HashMap<>();
    patches.put("directorId", 2L);
    Movie patchedMovie =
        Movie.builder()
            .id(1L)
            .title("Interstellar")
            .genre("Sci-Fi")
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(169)
            .director(newDirector)
            .build();
    MovieDTO patchedMovieDTO =
        MovieDTO.builder()
            .id(1L)
            .title("Interstellar")
            .genre("Sci-Fi")
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(169)
            .directorId(2L)
            .directorName("Steven Spielberg")
            .build();
    when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
    when(directorRepository.findById(2L)).thenReturn(Optional.of(newDirector));
    when(movieRepository.save(any(Movie.class))).thenReturn(patchedMovie);
    // Act
    MovieDTO result = movieService.patchMovie(1L, patches);
    // Assert
    assertEquals(patchedMovieDTO, result);
  }

  @Test
  void patchMovie_ShouldThrowException_WhenDirectorNotFound() {
    // Arrange
    Map<String, Object> patches = new HashMap<>();
    patches.put("directorId", 99L);
    when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
    when(directorRepository.findById(99L)).thenReturn(Optional.empty());
    // Act & Assert
    assertThrows(EntityNotFoundException.class, () -> movieService.patchMovie(1L, patches));
  }

  @Test
  void patchMovie_ShouldThrowException_WhenMovieNotFound() {
    // Arrange
    Map<String, Object> patches = new HashMap<>();
    patches.put("title", "New Title");
    when(movieRepository.findById(99L)).thenReturn(Optional.empty());
    // Act & Assert
    assertThrows(EntityNotFoundException.class, () -> movieService.patchMovie(99L, patches));
  }

  @Test
  void patchMovie_ShouldIgnoreUnknownFields() {
    // Arrange
    Map<String, Object> patches = new HashMap<>();
    patches.put("title", "Interstellar: New Title");
    patches.put("unknownField", "some value");
    Movie patchedMovie =
        Movie.builder()
            .id(1L)
            .title("Interstellar: New Title")
            .genre("Sci-Fi")
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(169)
            .director(director)
            .build();
    MovieDTO patchedMovieDTO =
        MovieDTO.builder()
            .id(1L)
            .title("Interstellar: New Title")
            .genre("Sci-Fi")
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(169)
            .directorId(1L)
            .directorName("Christopher Nolan")
            .build();
    when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
    when(movieRepository.save(any(Movie.class))).thenReturn(patchedMovie);
    // Act
    MovieDTO result = movieService.patchMovie(1L, patches);
    // Assert
    assertEquals(patchedMovieDTO, result);
  }
}
