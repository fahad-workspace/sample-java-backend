package com.sample.backend.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import com.sample.backend.dto.ActorDTO;
import com.sample.backend.dto.PagedResponse;
import com.sample.backend.exception.EntityNotFoundException;
import com.sample.backend.service.ActorService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ActorController.class)
class ActorControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private ActorService actorService;
  private ObjectMapper objectMapper;
  private ActorDTO actorDTO;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    actorDTO =
        ActorDTO.builder()
            .id(1L)
            .firstName("Brad")
            .lastName("Pitt")
            .birthDate(LocalDate.of(1963, 12, 18))
            .nationality("American")
            .build();
  }

  @Test
  void getAllActors_ShouldReturnAllActors() throws Exception {
    PagedResponse<ActorDTO> pagedResponse =
        new PagedResponse<>(List.of(actorDTO), 0, 10, 1, 1, true, true);
    when(actorService.getAllActors(0, 10, "id", "asc")).thenReturn(pagedResponse);
    mockMvc
        .perform(get("/api/actors"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.page", is(0)))
        .andExpect(jsonPath("$.size", is(10)))
        .andExpect(jsonPath("$.totalElements", is(1)))
        .andExpect(jsonPath("$.totalPages", is(1)))
        .andExpect(jsonPath("$.first", is(true)))
        .andExpect(jsonPath("$.last", is(true)))
        .andExpect(jsonPath("$.content[0].id", is(1)))
        .andExpect(jsonPath("$.content[0].firstName", is("Brad")))
        .andExpect(jsonPath("$.content[0].lastName", is("Pitt")))
        .andExpect(jsonPath("$.content[0].nationality", is("American")));
  }

  @Test
  void getActorById_ShouldReturnActor_WhenActorExists() throws Exception {
    when(actorService.getActorById(1L)).thenReturn(actorDTO);
    mockMvc
        .perform(get("/api/actors/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.firstName", is("Brad")))
        .andExpect(jsonPath("$.lastName", is("Pitt")))
        .andExpect(jsonPath("$.nationality", is("American")));
  }

  @Test
  void getActorById_ShouldReturnNotFound_WhenActorDoesNotExist() throws Exception {
    when(actorService.getActorById(99L))
        .thenThrow(new EntityNotFoundException("Actor not found with ID: 99"));
    mockMvc.perform(get("/api/actors/99")).andExpect(status().isNotFound());
  }

  @Test
  void searchActors_ShouldReturnMatchingActors() throws Exception {
    PagedResponse<ActorDTO> response =
        new PagedResponse<>(List.of(actorDTO), 1, 1, 1, 0, true, false);
    when(actorService.searchActors(eq("Pit"), anyInt(), anyInt())).thenReturn(response);
    mockMvc
        .perform(get("/api/actors/search?name=Pit"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].id", is(1)))
        .andExpect(jsonPath("$.content[0].firstName", is("Brad")))
        .andExpect(jsonPath("$.content[0].lastName", is("Pitt")))
        .andExpect(jsonPath("$.totalElements", is(1)));
  }

  @Test
  void createActor_ShouldReturnCreatedActor() throws Exception {
    ActorDTO newActor =
        ActorDTO.builder()
            .firstName("Tom")
            .lastName("Hanks")
            .birthDate(LocalDate.of(1956, 7, 9))
            .nationality("American")
            .build();
    ActorDTO createdActor =
        ActorDTO.builder()
            .id(2L)
            .firstName("Tom")
            .lastName("Hanks")
            .birthDate(LocalDate.of(1956, 7, 9))
            .nationality("American")
            .build();
    when(actorService.createActor(any(ActorDTO.class))).thenReturn(createdActor);
    mockMvc
        .perform(
            post("/api/actors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newActor)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(2)))
        .andExpect(jsonPath("$.firstName", is("Tom")))
        .andExpect(jsonPath("$.lastName", is("Hanks")))
        .andExpect(jsonPath("$.nationality", is("American")));
  }

  @Test
  void updateActor_ShouldReturnUpdatedActor_WhenActorExists() throws Exception {
    ActorDTO updateActor =
        ActorDTO.builder()
            .firstName("Bradley")
            .lastName("Pitt")
            .birthDate(LocalDate.of(1963, 12, 18))
            .nationality("American")
            .build();
    ActorDTO updatedActor =
        ActorDTO.builder()
            .id(1L)
            .firstName("Bradley")
            .lastName("Pitt")
            .birthDate(LocalDate.of(1963, 12, 18))
            .nationality("American")
            .build();
    when(actorService.updateActor(eq(1L), any(ActorDTO.class))).thenReturn(updatedActor);
    mockMvc
        .perform(
            put("/api/actors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateActor)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.firstName", is("Bradley")))
        .andExpect(jsonPath("$.lastName", is("Pitt")));
  }

  @Test
  void updateActor_ShouldReturnNotFound_WhenActorDoesNotExist() throws Exception {
    ActorDTO updateActor =
        ActorDTO.builder()
            .firstName("Bradley")
            .lastName("Pitt")
            .birthDate(LocalDate.of(1963, 12, 18))
            .nationality("American")
            .build();
    when(actorService.updateActor(eq(99L), any(ActorDTO.class)))
        .thenThrow(new EntityNotFoundException("Actor not found with ID: 99"));
    mockMvc
        .perform(
            put("/api/actors/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateActor)))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteActor_ShouldReturnNoContent_WhenActorExists() throws Exception {
    doNothing().when(actorService).deleteActor(1L);
    mockMvc.perform(delete("/api/actors/1")).andExpect(status().isNoContent());
  }

  @Test
  void deleteActor_ShouldReturnNotFound_WhenActorDoesNotExist() throws Exception {
    doThrow(new EntityNotFoundException("Actor not found with ID: 99"))
        .when(actorService)
        .deleteActor(99L);
    mockMvc.perform(delete("/api/actors/99")).andExpect(status().isNotFound());
  }
}
