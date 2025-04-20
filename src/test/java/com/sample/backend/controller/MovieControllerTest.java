package com.sample.backend.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sample.backend.dto.MovieDTO;
import com.sample.backend.exception.EntityNotFoundException;
import com.sample.backend.model.Genre;
import com.sample.backend.service.MovieService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private MovieService movieService;
  private ObjectMapper objectMapper;
  private MovieDTO movieDTO;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    movieDTO =
        MovieDTO.builder()
            .id(1L)
            .title("Interstellar")
            .genre(Genre.SCI_FI)
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(169)
            .directorId(1L)
            .directorName("Christopher Nolan")
            .build();
  }

  @Test
  void getAllMovies_ShouldReturnAllMovies() throws Exception {
    when(movieService.getAllMovies()).thenReturn(List.of(movieDTO));
    mockMvc
        .perform(get("/api/movies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].title", is("Interstellar")))
        .andExpect(jsonPath("$[0].genre", is(Genre.SCI_FI.name())))
        .andExpect(jsonPath("$[0].directorName", is("Christopher Nolan")));
  }

  @Test
  void getMovieById_ShouldReturnMovie_WhenMovieExists() throws Exception {
    when(movieService.getMovieById(1L)).thenReturn(movieDTO);
    mockMvc
        .perform(get("/api/movies/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.title", is("Interstellar")))
        .andExpect(jsonPath("$.genre", is(Genre.SCI_FI.name())))
        .andExpect(jsonPath("$.directorName", is("Christopher Nolan")));
  }

  @Test
  void getMovieById_ShouldReturnNotFound_WhenMovieDoesNotExist() throws Exception {
    when(movieService.getMovieById(99L))
        .thenThrow(new EntityNotFoundException("Movie not found with ID: 99"));
    mockMvc.perform(get("/api/movies/99")).andExpect(status().isNotFound());
  }

  @Test
  void searchMovies_ByTitle_ShouldReturnMatchingMovies() throws Exception {
    when(movieService.getMoviesByTitle("Inter")).thenReturn(List.of(movieDTO));
    mockMvc
        .perform(get("/api/movies/search?title=Inter"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].title", is("Interstellar")));
  }

  @Test
  void searchMovies_ByGenre_ShouldReturnMatchingMovies() throws Exception {
    // Update the mock to use the Genre enum
    when(movieService.getMoviesByGenre(Genre.SCI_FI)).thenReturn(List.of(movieDTO));
    // If using the enum display name approach, use the URL parameter that matches how your
    // controller expects it
    mockMvc
        .perform(get("/api/movies/search?genre=" + Genre.SCI_FI.name()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].title", is("Interstellar")));
  }

  @Test
  void searchMovies_NoParams_ShouldReturnAllMovies() throws Exception {
    when(movieService.getAllMovies()).thenReturn(List.of(movieDTO));
    mockMvc
        .perform(get("/api/movies/search"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].title", is("Interstellar")));
  }

  @Test
  void createMovie_ShouldReturnCreatedMovie() throws Exception {
    MovieDTO newMovie =
        MovieDTO.builder()
            .title("Tenet")
            .genre(Genre.ACTION)
            .releaseDate(LocalDate.of(2020, 9, 3))
            .durationMinutes(150)
            .directorId(1L)
            .build();
    MovieDTO createdMovie =
        MovieDTO.builder()
            .id(2L)
            .title("Tenet")
            .genre(Genre.ACTION)
            .releaseDate(LocalDate.of(2020, 9, 3))
            .durationMinutes(150)
            .directorId(1L)
            .directorName("Christopher Nolan")
            .build();
    when(movieService.createMovie(any(MovieDTO.class))).thenReturn(createdMovie);
    mockMvc
        .perform(
            post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newMovie)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(2)))
        .andExpect(jsonPath("$.title", is("Tenet")))
        .andExpect(jsonPath("$.genre", is(Genre.ACTION.name())))
        .andExpect(jsonPath("$.directorName", is("Christopher Nolan")));
  }

  @Test
  void updateMovie_ShouldReturnUpdatedMovie_WhenMovieExists() throws Exception {
    MovieDTO updateMovie =
        MovieDTO.builder()
            .title("Interstellar: Extended Edition")
            .genre(Genre.SCI_FI)
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(180)
            .directorId(1L)
            .build();
    MovieDTO updatedMovie =
        MovieDTO.builder()
            .id(1L)
            .title("Interstellar: Extended Edition")
            .genre(Genre.SCI_FI)
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(180)
            .directorId(1L)
            .directorName("Christopher Nolan")
            .build();
    when(movieService.updateMovie(eq(1L), any(MovieDTO.class))).thenReturn(updatedMovie);
    mockMvc
        .perform(
            put("/api/movies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateMovie)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.title", is("Interstellar: Extended Edition")))
        .andExpect(jsonPath("$.durationMinutes", is(180)));
  }

  @Test
  void updateMovie_ShouldReturnNotFound_WhenMovieDoesNotExist() throws Exception {
    MovieDTO updateMovie =
        MovieDTO.builder()
            .title("Interstellar: Extended Edition")
            .genre(Genre.SCI_FI)
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(180)
            .directorId(1L)
            .build();
    when(movieService.updateMovie(eq(99L), any(MovieDTO.class)))
        .thenThrow(new EntityNotFoundException("Movie not found with ID: 99"));
    mockMvc
        .perform(
            put("/api/movies/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateMovie)))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteMovie_ShouldReturnNoContent_WhenMovieExists() throws Exception {
    doNothing().when(movieService).deleteMovie(1L);
    mockMvc.perform(delete("/api/movies/1")).andExpect(status().isNoContent());
  }

  @Test
  void deleteMovie_ShouldReturnNotFound_WhenMovieDoesNotExist() throws Exception {
    doThrow(new EntityNotFoundException("Movie not found with ID: 99"))
        .when(movieService)
        .deleteMovie(99L);
    mockMvc.perform(delete("/api/movies/99")).andExpect(status().isNotFound());
  }

  @Test
  void patchMovie_ShouldReturnUpdatedMovie_WhenMovieExists() throws Exception {
    Map<String, Object> patchValues = new HashMap<>();
    patchValues.put("title", "Interstellar: Director's Cut");
    patchValues.put("durationMinutes", 180);
    MovieDTO patchedMovie =
        MovieDTO.builder()
            .id(1L)
            .title("Interstellar: Director's Cut")
            .genre(Genre.SCI_FI)
            .releaseDate(LocalDate.of(2014, 11, 7))
            .durationMinutes(180)
            .directorId(1L)
            .directorName("Christopher Nolan")
            .build();
    when(movieService.patchMovie(eq(1L), any(Map.class))).thenReturn(patchedMovie);
    mockMvc
        .perform(
            patch("/api/movies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchValues)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.title", is("Interstellar: Director's Cut")))
        .andExpect(jsonPath("$.durationMinutes", is(180)))
        .andExpect(jsonPath("$.genre", is(Genre.SCI_FI.name())));
  }

  @Test
  void patchMovie_ShouldReturnNotFound_WhenMovieDoesNotExist() throws Exception {
    Map<String, Object> patchValues = new HashMap<>();
    patchValues.put("title", "Non-existent Movie");
    when(movieService.patchMovie(eq(99L), any(Map.class)))
        .thenThrow(new EntityNotFoundException("Movie not found with ID: 99"));
    mockMvc
        .perform(
            patch("/api/movies/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchValues)))
        .andExpect(status().isNotFound());
  }
}
