package com.sample.backend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sample.backend.dto.RoleDTO;
import com.sample.backend.model.Actor;
import com.sample.backend.model.Movie;
import com.sample.backend.model.Role;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleMapperTest {

  private Role role;
  private Movie movie;
  private Actor actor;

  @BeforeEach
  void setUp() {
    movie =
        Movie.builder()
            .id(1L)
            .title("Inception")
            .genre("Sci-Fi")
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
  }

  @Test
  void toDTO_ShouldConvertEntityToDTO() {
    RoleDTO result = RoleMapper.toDTO(role);
    assertEquals(role.getId(), result.id());
    assertEquals(role.getCharacterName(), result.characterName());
    assertEquals(role.getMovie().getId(), result.movieId());
    assertEquals(role.getMovie().getTitle(), result.movieTitle());
    assertEquals(role.getActor().getId(), result.actorId());
    assertEquals("Leonardo DiCaprio", result.actorName());
  }

  @Test
  void toDTO_ShouldHandleNullMovieAndActor() {
    Role roleWithoutMovieAndActor = Role.builder().id(1L).characterName("Dom Cobb").build();
    RoleDTO result = RoleMapper.toDTO(roleWithoutMovieAndActor);
    assertEquals(roleWithoutMovieAndActor.getId(), result.id());
    assertEquals(roleWithoutMovieAndActor.getCharacterName(), result.characterName());
    assertNull(result.movieId());
    assertNull(result.movieTitle());
    assertNull(result.actorId());
    assertNull(result.actorName());
  }

  @Test
  void toDTO_ShouldReturnNull_WhenEntityIsNull() {
    assertNull(null);
  }

  @Test
  void updateRoleFromDTO_ShouldUpdateOnlyProvidedFields() {
    RoleDTO partialDTO = RoleDTO.builder().characterName("Dominick Cobb").build();
    Role existingRole =
        Role.builder().id(1L).characterName("Dom Cobb").movie(movie).actor(actor).build();
    RoleMapper.updateRoleFromDTO(existingRole, partialDTO);
    assertEquals("Dominick Cobb", existingRole.getCharacterName());
    assertEquals(movie, existingRole.getMovie());
    assertEquals(actor, existingRole.getActor());
  }

  @Test
  void updateRoleFromDTO_ShouldNotModifyWhenNull() {
    RoleDTO partialDTO = RoleDTO.builder().build();
    Role existingRole =
        Role.builder().id(1L).characterName("Dom Cobb").movie(movie).actor(actor).build();
    RoleMapper.updateRoleFromDTO(existingRole, partialDTO);
    assertEquals("Dom Cobb", existingRole.getCharacterName());
    assertEquals(movie, existingRole.getMovie());
    assertEquals(actor, existingRole.getActor());
  }
}
