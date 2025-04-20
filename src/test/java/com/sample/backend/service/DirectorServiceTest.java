package com.sample.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sample.backend.dto.DirectorDTO;
import com.sample.backend.exception.EntityNotFoundException;
import com.sample.backend.model.Director;
import com.sample.backend.repository.DirectorRepository;
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
class DirectorServiceTest {

  @Mock private DirectorRepository directorRepository;
  @InjectMocks private DirectorService directorService;
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
  void getAllDirectors_ShouldReturnList() {
    when(directorRepository.findAll()).thenReturn(List.of(director));
    List<DirectorDTO> result = directorService.getAllDirectors();
    assertEquals(1, result.size());
    assertEquals(directorDTO, result.getFirst());
  }

  @Test
  void getDirectorById_ShouldReturnDirector_WhenDirectorExists() {
    when(directorRepository.findById(1L)).thenReturn(Optional.of(director));
    DirectorDTO result = directorService.getDirectorById(1L);
    assertEquals(directorDTO, result);
  }

  @Test
  void getDirectorById_ShouldThrowException_WhenDirectorDoesNotExist() {
    when(directorRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> directorService.getDirectorById(99L));
  }

  @Test
  void searchDirectors_ShouldReturnMatchingDirectors() {
    when(directorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            "Spiel", "Spiel"))
        .thenReturn(List.of(director));
    List<DirectorDTO> result = directorService.searchDirectors("Spiel");
    assertEquals(1, result.size());
    assertEquals(directorDTO, result.getFirst());
  }

  @Test
  void createDirector_ShouldReturnCreatedDirector() {
    Director newDirector =
        Director.builder()
            .firstName("Steven")
            .lastName("Spielberg")
            .birthDate(LocalDate.of(1946, 12, 18))
            .nationality("American")
            .build();
    when(directorRepository.save(newDirector)).thenReturn(director);
    DirectorDTO result = directorService.createDirector(directorDTO);
    assertEquals(directorDTO, result);
  }

  @Test
  void updateDirector_FullUpdate_ShouldReturnUpdatedDirector() {
    DirectorDTO fullUpdateDTO =
        DirectorDTO.builder()
            .id(1L)
            .firstName("Steven")
            .lastName("Spielberg")
            .birthDate(LocalDate.of(1946, 12, 18))
            .nationality("American")
            .build();
    Director updatedDirector =
        Director.builder()
            .id(1L)
            .firstName("Steven")
            .lastName("Spielberg")
            .birthDate(LocalDate.of(1946, 12, 18))
            .nationality("American")
            .build();
    when(directorRepository.findById(1L)).thenReturn(Optional.of(director));
    when(directorRepository.save(any(Director.class))).thenReturn(updatedDirector);
    DirectorDTO result = directorService.updateDirector(1L, fullUpdateDTO);
    assertEquals(fullUpdateDTO, result);
  }

  @Test
  void updateDirector_PartialUpdate_ShouldReturnPartiallyUpdatedDirector() {
    DirectorDTO partialUpdateDTO = DirectorDTO.builder().nationality("American-German").build();
    Director updatedDirector =
        Director.builder()
            .id(1L)
            .firstName("Steven")
            .lastName("Spielberg")
            .birthDate(LocalDate.of(1946, 12, 18))
            .nationality("American-German")
            .build();
    DirectorDTO updatedDirectorDTO =
        DirectorDTO.builder()
            .id(1L)
            .firstName("Steven")
            .lastName("Spielberg")
            .birthDate(LocalDate.of(1946, 12, 18))
            .nationality("American-German")
            .build();
    when(directorRepository.findById(1L)).thenReturn(Optional.of(director));
    when(directorRepository.save(any(Director.class))).thenReturn(updatedDirector);
    DirectorDTO result = directorService.updateDirector(1L, partialUpdateDTO);
    assertEquals(updatedDirectorDTO, result);
  }

  @Test
  void deleteDirector_ShouldDeleteDirector_WhenDirectorExists() {
    when(directorRepository.existsById(1L)).thenReturn(true);
    doNothing().when(directorRepository).deleteById(1L);
    directorService.deleteDirector(1L);
    verify(directorRepository).deleteById(1L);
  }

  @Test
  void deleteDirector_ShouldThrowException_WhenDirectorDoesNotExist() {
    when(directorRepository.existsById(99L)).thenReturn(false);
    assertThrows(EntityNotFoundException.class, () -> directorService.deleteDirector(99L));
  }
}
