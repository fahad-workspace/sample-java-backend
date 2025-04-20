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
import com.sample.backend.dto.RoleDTO;
import com.sample.backend.exception.EntityNotFoundException;
import com.sample.backend.service.RoleService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RoleController.class)
class RoleControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockitoBean private RoleService roleService;
  private ObjectMapper objectMapper;
  private RoleDTO roleDTO;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    roleDTO =
        RoleDTO.builder()
            .id(1L)
            .characterName("Dom Cobb")
            .movieId(1L)
            .movieTitle("Inception")
            .actorId(1L)
            .actorName("Leonardo DiCaprio")
            .build();
  }

  @Test
  void getAllRoles_ShouldReturnAllRoles() throws Exception {
    when(roleService.getAllRoles()).thenReturn(List.of(roleDTO));
    mockMvc
        .perform(get("/api/roles"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].characterName", is("Dom Cobb")))
        .andExpect(jsonPath("$[0].movieTitle", is("Inception")))
        .andExpect(jsonPath("$[0].actorName", is("Leonardo DiCaprio")));
  }

  @Test
  void getRoleById_ShouldReturnRole_WhenRoleExists() throws Exception {
    when(roleService.getRoleById(1L)).thenReturn(roleDTO);
    mockMvc
        .perform(get("/api/roles/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.characterName", is("Dom Cobb")))
        .andExpect(jsonPath("$.movieTitle", is("Inception")))
        .andExpect(jsonPath("$.actorName", is("Leonardo DiCaprio")));
  }

  @Test
  void getRoleById_ShouldReturnNotFound_WhenRoleDoesNotExist() throws Exception {
    when(roleService.getRoleById(99L))
        .thenThrow(new EntityNotFoundException("Role not found with ID: 99"));
    mockMvc.perform(get("/api/roles/99")).andExpect(status().isNotFound());
  }

  @Test
  void getRolesByMovie_ShouldReturnRolesForMovie() throws Exception {
    when(roleService.getRolesByMovieId(1L)).thenReturn(List.of(roleDTO));
    mockMvc
        .perform(get("/api/roles/movie/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].characterName", is("Dom Cobb")))
        .andExpect(jsonPath("$[0].movieTitle", is("Inception")));
  }

  @Test
  void getRolesByActor_ShouldReturnRolesForActor() throws Exception {
    when(roleService.getRolesByActorId(1L)).thenReturn(List.of(roleDTO));
    mockMvc
        .perform(get("/api/roles/actor/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].characterName", is("Dom Cobb")))
        .andExpect(jsonPath("$[0].actorName", is("Leonardo DiCaprio")));
  }

  @Test
  void searchRoles_ShouldReturnMatchingRoles() throws Exception {
    when(roleService.searchRolesByCharacterName("Cobb")).thenReturn(List.of(roleDTO));
    mockMvc
        .perform(get("/api/roles/search?characterName=Cobb"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].characterName", is("Dom Cobb")));
  }

  @Test
  void createRole_ShouldReturnCreatedRole() throws Exception {
    RoleDTO newRole = RoleDTO.builder().characterName("Arthur").movieId(1L).actorId(2L).build();
    RoleDTO createdRole =
        RoleDTO.builder()
            .id(2L)
            .characterName("Arthur")
            .movieId(1L)
            .movieTitle("Inception")
            .actorId(2L)
            .actorName("Joseph Gordon-Levitt")
            .build();
    when(roleService.createRole(any(RoleDTO.class))).thenReturn(createdRole);
    mockMvc
        .perform(
            post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRole)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(2)))
        .andExpect(jsonPath("$.characterName", is("Arthur")))
        .andExpect(jsonPath("$.movieTitle", is("Inception")))
        .andExpect(jsonPath("$.actorName", is("Joseph Gordon-Levitt")));
  }

  @Test
  void updateRole_ShouldReturnUpdatedRole_WhenRoleExists() throws Exception {
    RoleDTO updateRole =
        RoleDTO.builder().characterName("Dominick Cobb").movieId(1L).actorId(1L).build();
    RoleDTO updatedRole =
        RoleDTO.builder()
            .id(1L)
            .characterName("Dominick Cobb")
            .movieId(1L)
            .movieTitle("Inception")
            .actorId(1L)
            .actorName("Leonardo DiCaprio")
            .build();
    when(roleService.updateRole(eq(1L), any(RoleDTO.class))).thenReturn(updatedRole);
    mockMvc
        .perform(
            put("/api/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRole)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.characterName", is("Dominick Cobb")));
  }

  @Test
  void updateRole_ShouldReturnNotFound_WhenRoleDoesNotExist() throws Exception {
    RoleDTO updateRole =
        RoleDTO.builder().characterName("Dominick Cobb").movieId(1L).actorId(1L).build();
    when(roleService.updateRole(eq(99L), any(RoleDTO.class)))
        .thenThrow(new EntityNotFoundException("Role not found with ID: 99"));
    mockMvc
        .perform(
            put("/api/roles/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRole)))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteRole_ShouldReturnNoContent_WhenRoleExists() throws Exception {
    doNothing().when(roleService).deleteRole(1L);
    mockMvc.perform(delete("/api/roles/1")).andExpect(status().isNoContent());
  }

  @Test
  void deleteRole_ShouldReturnNotFound_WhenRoleDoesNotExist() throws Exception {
    doThrow(new EntityNotFoundException("Role not found with ID: 99"))
        .when(roleService)
        .deleteRole(99L);
    mockMvc.perform(delete("/api/roles/99")).andExpect(status().isNotFound());
  }
}
