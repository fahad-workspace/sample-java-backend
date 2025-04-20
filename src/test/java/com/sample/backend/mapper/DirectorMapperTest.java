package com.sample.backend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sample.backend.dto.DirectorDTO;
import com.sample.backend.model.Director;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DirectorMapperTest {

  private Director director;
  private DirectorDTO directorDTO;

  @BeforeEach
  void setUp() {
    director =
        Director.builder()
            .id(1L)
            .firstName("Steven")
            .lastName("Spielberg")
            .birthDate(LocalDate.of(1946, 12, 18))
            .nationality("American")
            .build();
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
  void toDTO_ShouldConvertEntityToDTO() {
    DirectorDTO result = DirectorMapper.toDTO(director);
    assertEquals(director.getId(), result.id());
    assertEquals(director.getFirstName(), result.firstName());
    assertEquals(director.getLastName(), result.lastName());
    assertEquals(director.getBirthDate(), result.birthDate());
    assertEquals(director.getNationality(), result.nationality());
  }

  @Test
  void toDTO_ShouldReturnNull_WhenEntityIsNull() {
    assertNull(null);
  }

  @Test
  void toEntity_ShouldConvertDTOToEntity() {
    Director result = DirectorMapper.toEntity(directorDTO);
    assertNull(result.getId());
    assertEquals(directorDTO.firstName(), result.getFirstName());
    assertEquals(directorDTO.lastName(), result.getLastName());
    assertEquals(directorDTO.birthDate(), result.getBirthDate());
    assertEquals(directorDTO.nationality(), result.getNationality());
  }

  @Test
  void toEntity_ShouldReturnNull_WhenDTOIsNull() {
    assertNull(null);
  }

  @Test
  void updateDirectorFromDTO_ShouldUpdateOnlyProvidedFields() {
    DirectorDTO partialDTO = DirectorDTO.builder().firstName("Steven Allan").build();
    Director existingDirector =
        Director.builder()
            .id(1L)
            .firstName("Steven")
            .lastName("Spielberg")
            .birthDate(LocalDate.of(1946, 12, 18))
            .nationality("American")
            .build();
    DirectorMapper.updateDirectorFromDTO(existingDirector, partialDTO);
    assertEquals("Steven Allan", existingDirector.getFirstName());
    assertEquals("Spielberg", existingDirector.getLastName());
    assertEquals(LocalDate.of(1946, 12, 18), existingDirector.getBirthDate());
    assertEquals("American", existingDirector.getNationality());
  }

  @Test
  void updateDirectorFromDTO_ShouldUpdateAllFields() {
    DirectorDTO fullDTO =
        DirectorDTO.builder()
            .firstName("Steven Allan")
            .lastName("Spielberg Jr.")
            .birthDate(LocalDate.of(1946, 12, 19))
            .nationality("American-German")
            .build();
    Director existingDirector =
        Director.builder()
            .id(1L)
            .firstName("Steven")
            .lastName("Spielberg")
            .birthDate(LocalDate.of(1946, 12, 18))
            .nationality("American")
            .build();
    DirectorMapper.updateDirectorFromDTO(existingDirector, fullDTO);
    assertEquals("Steven Allan", existingDirector.getFirstName());
    assertEquals("Spielberg Jr.", existingDirector.getLastName());
    assertEquals(LocalDate.of(1946, 12, 19), existingDirector.getBirthDate());
    assertEquals("American-German", existingDirector.getNationality());
  }
}
