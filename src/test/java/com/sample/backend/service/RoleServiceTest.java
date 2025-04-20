package com.sample.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sample.backend.dto.RoleDTO;
import com.sample.backend.exception.EntityNotFoundException;
import com.sample.backend.model.Actor;
import com.sample.backend.model.Genre;
import com.sample.backend.model.Movie;
import com.sample.backend.model.Role;
import com.sample.backend.repository.ActorRepository;
import com.sample.backend.repository.MovieRepository;
import com.sample.backend.repository.RoleRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

  @Mock private RoleRepository roleRepository;
  @Mock private MovieRepository movieRepository;
  @Mock private ActorRepository actorRepository;
  @InjectMocks private RoleService roleService;
  private Role role;
  private RoleDTO roleDTO;
  private Movie movie;
  private Actor actor;

  @BeforeEach
  void setUp() {
    movie =
        Movie.builder()
            .id(1L)
            .title("Inception")
            .genre(Genre.SCI_FI)
            .releaseDate(LocalDate.of(2010, 7, 16))
            .durationMinutes(148)
            .build();
    actor =
        Actor.builder()
            .id(1L)
            .firstName("Leonardo")
            .lastName("DiCaprio")
            .birthDate(LocalDate.of(1974, 11, 11))
            .nationality("American")
            .build();
    role = Role.builder().id(1L).characterName("Dom Cobb").movie(movie).actor(actor).build();
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
  void getAllRoles_ShouldReturnList() {
    when(roleRepository.findAll()).thenReturn(List.of(role));
    List<RoleDTO> result = roleService.getAllRoles();
    assertEquals(1, result.size());
    assertEquals(roleDTO, result.getFirst());
  }

  @Test
  void getRoleById_ShouldReturnRole_WhenRoleExists() {
    when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
    RoleDTO result = roleService.getRoleById(1L);
    assertEquals(roleDTO, result);
  }

  @Test
  void getRoleById_ShouldThrowException_WhenRoleDoesNotExist() {
    when(roleRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> roleService.getRoleById(99L));
  }

  @Test
  void getRolesByMovieId_ShouldReturnMatchingRoles() {
    when(roleRepository.findByMovieId(1L)).thenReturn(List.of(role));
    List<RoleDTO> result = roleService.getRolesByMovieId(1L);
    assertEquals(1, result.size());
    assertEquals(roleDTO, result.getFirst());
  }

  @Test
  void getRolesByActorId_ShouldReturnMatchingRoles() {
    when(roleRepository.findByActorId(1L)).thenReturn(List.of(role));
    List<RoleDTO> result = roleService.getRolesByActorId(1L);
    assertEquals(1, result.size());
    assertEquals(roleDTO, result.getFirst());
  }

  @Test
  void searchRolesByCharacterName_ShouldReturnMatchingRoles() {
    when(roleRepository.findByCharacterNameContainingIgnoreCase("Cobb")).thenReturn(List.of(role));
    List<RoleDTO> result = roleService.searchRolesByCharacterName("Cobb");
    assertEquals(1, result.size());
    assertEquals(roleDTO, result.getFirst());
  }

  @Test
  void createRole_ShouldReturnCreatedRole() {
    RoleDTO newRoleDTO = RoleDTO.builder().characterName("Arthur").movieId(1L).actorId(2L).build();
    Actor joseph = Actor.builder().id(2L).firstName("Joseph").lastName("Gordon-Levitt").build();
    Role savedRole =
        Role.builder().id(2L).characterName("Arthur").movie(movie).actor(joseph).build();
    RoleDTO savedRoleDTO =
        RoleDTO.builder()
            .id(2L)
            .characterName("Arthur")
            .movieId(1L)
            .movieTitle("Inception")
            .actorId(2L)
            .actorName("Joseph Gordon-Levitt")
            .build();
    when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
    when(actorRepository.findById(2L)).thenReturn(Optional.of(joseph));
    when(roleRepository.save(any(Role.class))).thenReturn(savedRole);
    RoleDTO result = roleService.createRole(newRoleDTO);
    assertEquals(savedRoleDTO, result);
  }

  @Test
  void createRole_ShouldThrowException_WhenMovieNotFound() {
    RoleDTO newRoleDTO = RoleDTO.builder().characterName("Arthur").movieId(99L).actorId(1L).build();
    when(movieRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> roleService.createRole(newRoleDTO));
  }

  @Test
  void createRole_ShouldThrowException_WhenActorNotFound() {
    RoleDTO newRoleDTO = RoleDTO.builder().characterName("Arthur").movieId(1L).actorId(99L).build();
    when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
    when(actorRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> roleService.createRole(newRoleDTO));
  }

  @Test
  void updateRole_ShouldReturnUpdatedRole() {
    RoleDTO updateRoleDTO =
        RoleDTO.builder().characterName("Dominick Cobb").movieId(1L).actorId(1L).build();
    Role updatedRole =
        Role.builder().id(1L).characterName("Dominick Cobb").movie(movie).actor(actor).build();
    RoleDTO updatedRoleDTO =
        RoleDTO.builder()
            .id(1L)
            .characterName("Dominick Cobb")
            .movieId(1L)
            .movieTitle("Inception")
            .actorId(1L)
            .actorName("Leonardo DiCaprio")
            .build();
    when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
    when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
    when(actorRepository.findById(1L)).thenReturn(Optional.of(actor));
    when(roleRepository.save(any(Role.class))).thenReturn(updatedRole);
    RoleDTO result = roleService.updateRole(1L, updateRoleDTO);
    assertEquals(updatedRoleDTO, result);
  }

  @Test
  void deleteRole_ShouldDeleteRole_WhenRoleExists() {
    when(roleRepository.existsById(1L)).thenReturn(true);
    doNothing().when(roleRepository).deleteById(1L);
    roleService.deleteRole(1L);
    verify(roleRepository).deleteById(1L);
  }

  @Test
  void deleteRole_ShouldThrowException_WhenRoleDoesNotExist() {
    when(roleRepository.existsById(99L)).thenReturn(false);
    assertThrows(EntityNotFoundException.class, () -> roleService.deleteRole(99L));
  }
}
