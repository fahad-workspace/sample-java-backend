package com.sample.backend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sample.backend.dto.ActorDTO;
import com.sample.backend.model.Actor;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActorMapperTest {

  private Actor actor;
  private ActorDTO actorDTO;

  @BeforeEach
  void setUp() {
    actor =
        Actor.builder()
            .id(1L)
            .firstName("Brad")
            .lastName("Pitt")
            .birthDate(LocalDate.of(1963, 12, 18))
            .nationality("American")
            .build();
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
  void toDTO_ShouldConvertEntityToDTO() {
    ActorDTO result = ActorMapper.toDTO(actor);
    assertEquals(actor.getId(), result.id());
    assertEquals(actor.getFirstName(), result.firstName());
    assertEquals(actor.getLastName(), result.lastName());
    assertEquals(actor.getBirthDate(), result.birthDate());
    assertEquals(actor.getNationality(), result.nationality());
  }

  @Test
  void toDTO_ShouldReturnNull_WhenEntityIsNull() {
    assertNull(null);
  }

  @Test
  void toEntity_ShouldConvertDTOToEntity() {
    Actor result = ActorMapper.toEntity(actorDTO);
    // ID should not be set in the toEntity method
    assertNull(result.getId());
    assertEquals(actorDTO.firstName(), result.getFirstName());
    assertEquals(actorDTO.lastName(), result.getLastName());
    assertEquals(actorDTO.birthDate(), result.getBirthDate());
    assertEquals(actorDTO.nationality(), result.getNationality());
  }

  @Test
  void toEntity_ShouldReturnNull_WhenDTOIsNull() {
    assertNull(null);
  }

  @Test
  void updateActorFromDTO_ShouldUpdateOnlyProvidedFields() {
    ActorDTO partialDTO = ActorDTO.builder().firstName("Bradley").build();
    Actor existingActor =
        Actor.builder()
            .id(1L)
            .firstName("Brad")
            .lastName("Pitt")
            .birthDate(LocalDate.of(1963, 12, 18))
            .nationality("American")
            .build();
    ActorMapper.updateActorFromDTO(existingActor, partialDTO);
    assertEquals("Bradley", existingActor.getFirstName());
    assertEquals("Pitt", existingActor.getLastName());
    assertEquals(LocalDate.of(1963, 12, 18), existingActor.getBirthDate());
    assertEquals("American", existingActor.getNationality());
  }

  @Test
  void updateActorFromDTO_ShouldUpdateAllFields() {
    ActorDTO fullDTO =
        ActorDTO.builder()
            .firstName("Bradley")
            .lastName("Pitts")
            .birthDate(LocalDate.of(1963, 12, 19))
            .nationality("American-French")
            .build();
    Actor existingActor =
        Actor.builder()
            .id(1L)
            .firstName("Brad")
            .lastName("Pitt")
            .birthDate(LocalDate.of(1963, 12, 18))
            .nationality("American")
            .build();
    ActorMapper.updateActorFromDTO(existingActor, fullDTO);
    assertEquals("Bradley", existingActor.getFirstName());
    assertEquals("Pitts", existingActor.getLastName());
    assertEquals(LocalDate.of(1963, 12, 19), existingActor.getBirthDate());
    assertEquals("American-French", existingActor.getNationality());
  }
}
