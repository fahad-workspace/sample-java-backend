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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sample.backend.dto.DirectorDTO;
import com.sample.backend.exception.EntityNotFoundException;
import com.sample.backend.service.DirectorService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DirectorController.class)
class DirectorControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private DirectorService directorService;
  private ObjectMapper objectMapper;
  private DirectorDTO directorDTO;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    directorDTO =
        DirectorDTO.builder()
            .id(1L)
            .firstName("Steven")
            .lastName("Spielberg")
            .birthDate(LocalDate.of(1946, 12, 18))
            .nationality("American")
            .build();
  }

  @Test
  void getAllDirectors_ShouldReturnAllDirectors() throws Exception {
    when(directorService.getAllDirectors()).thenReturn(List.of(directorDTO));
    mockMvc
        .perform(get("/api/directors"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].firstName", is("Steven")))
        .andExpect(jsonPath("$[0].lastName", is("Spielberg")))
        .andExpect(jsonPath("$[0].nationality", is("American")));
  }

  @Test
  void getDirectorById_ShouldReturnDirector_WhenDirectorExists() throws Exception {
    when(directorService.getDirectorById(1L)).thenReturn(directorDTO);
    mockMvc
        .perform(get("/api/directors/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.firstName", is("Steven")))
        .andExpect(jsonPath("$.lastName", is("Spielberg")))
        .andExpect(jsonPath("$.nationality", is("American")));
  }

  @Test
  void getDirectorById_ShouldReturnNotFound_WhenDirectorDoesNotExist() throws Exception {
    when(directorService.getDirectorById(99L))
        .thenThrow(new EntityNotFoundException("Director not found with ID: 99"));
    mockMvc.perform(get("/api/directors/99")).andExpect(status().isNotFound());
  }

  @Test
  void searchDirectors_ShouldReturnMatchingDirectors() throws Exception {
    when(directorService.searchDirectors("Spiel")).thenReturn(List.of(directorDTO));
    mockMvc
        .perform(get("/api/directors/search?name=Spiel"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].firstName", is("Steven")))
        .andExpect(jsonPath("$[0].lastName", is("Spielberg")));
  }

  @Test
  void createDirector_ShouldReturnCreatedDirector() throws Exception {
    DirectorDTO newDirector =
        DirectorDTO.builder()
            .firstName("Martin")
            .lastName("Scorsese")
            .birthDate(LocalDate.of(1942, 11, 17))
            .nationality("American")
            .build();
    DirectorDTO createdDirector =
        DirectorDTO.builder()
            .id(2L)
            .firstName("Martin")
            .lastName("Scorsese")
            .birthDate(LocalDate.of(1942, 11, 17))
            .nationality("American")
            .build();
    when(directorService.createDirector(any(DirectorDTO.class))).thenReturn(createdDirector);
    mockMvc
        .perform(
            post("/api/directors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDirector)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(2)))
        .andExpect(jsonPath("$.firstName", is("Martin")))
        .andExpect(jsonPath("$.lastName", is("Scorsese")))
        .andExpect(jsonPath("$.nationality", is("American")));
  }

  @Test
  void updateDirector_ShouldReturnUpdatedDirector_WhenDirectorExists() throws Exception {
    DirectorDTO updateDirector =
        DirectorDTO.builder()
            .firstName("Steven Allan")
            .lastName("Spielberg")
            .birthDate(LocalDate.of(1946, 12, 18))
            .nationality("American")
            .build();
    DirectorDTO updatedDirector =
        DirectorDTO.builder()
            .id(1L)
            .firstName("Steven Allan")
            .lastName("Spielberg")
            .birthDate(LocalDate.of(1946, 12, 18))
            .nationality("American")
            .build();
    when(directorService.updateDirector(eq(1L), any(DirectorDTO.class)))
        .thenReturn(updatedDirector);
    mockMvc
        .perform(
            put("/api/directors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDirector)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.firstName", is("Steven Allan")))
        .andExpect(jsonPath("$.lastName", is("Spielberg")));
  }

  @Test
  void updateDirector_ShouldReturnNotFound_WhenDirectorDoesNotExist() throws Exception {
    DirectorDTO updateDirector =
        DirectorDTO.builder()
            .firstName("Steven Allan")
            .lastName("Spielberg")
            .birthDate(LocalDate.of(1946, 12, 18))
            .nationality("American")
            .build();
    when(directorService.updateDirector(eq(99L), any(DirectorDTO.class)))
        .thenThrow(new EntityNotFoundException("Director not found with ID: 99"));
    mockMvc
        .perform(
            put("/api/directors/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDirector)))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteDirector_ShouldReturnNoContent_WhenDirectorExists() throws Exception {
    doNothing().when(directorService).deleteDirector(1L);
    mockMvc.perform(delete("/api/directors/1")).andExpect(status().isNoContent());
  }

  @Test
  void deleteDirector_ShouldReturnNotFound_WhenDirectorDoesNotExist() throws Exception {
    doThrow(new EntityNotFoundException("Director not found with ID: 99"))
        .when(directorService)
        .deleteDirector(99L);
    mockMvc.perform(delete("/api/directors/99")).andExpect(status().isNotFound());
  }
}
