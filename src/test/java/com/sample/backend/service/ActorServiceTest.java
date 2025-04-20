package com.sample.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sample.backend.dto.ActorDTO;
import com.sample.backend.dto.PagedResponse;
import com.sample.backend.exception.EntityNotFoundException;
import com.sample.backend.model.Actor;
import com.sample.backend.repository.ActorRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class ActorServiceTest {

  @Mock private ActorRepository actorRepository;
  @InjectMocks private ActorService actorService;
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
  void getAllActors_ShouldReturnPagedResponse() {
    Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
    Page<Actor> actorPage = new PageImpl<>(List.of(actor), pageable, 1);
    when(actorRepository.findAll(pageable)).thenReturn(actorPage);
    PagedResponse<ActorDTO> result = actorService.getAllActors(0, 10, "id", "ASC");
    assertEquals(1, result.content().size());
    assertEquals(actorDTO, result.content().getFirst());
    assertEquals(1, result.totalElements());
    assertEquals(1, result.totalPages());
  }

  @Test
  void getActorById_ShouldReturnActor_WhenActorExists() {
    when(actorRepository.findById(1L)).thenReturn(Optional.of(actor));
    ActorDTO result = actorService.getActorById(1L);
    assertEquals(actorDTO, result);
  }

  @Test
  void getActorById_ShouldThrowException_WhenActorDoesNotExist() {
    when(actorRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> actorService.getActorById(99L));
  }

  @Test
  void searchActors_ShouldReturnMatchingActors() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<Actor> actorPage = new PageImpl<>(List.of(actor), pageable, 1);
    when(actorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            "Pit", "Pit", pageable))
        .thenReturn(actorPage);
    PagedResponse<ActorDTO> result = actorService.searchActors("Pit", 0, 10);
    assertEquals(1, result.content().size());
    assertEquals(actorDTO, result.content().getFirst());
    assertEquals(1, result.totalElements());
    assertEquals(1, result.totalPages());
  }

  @Test
  void createActor_ShouldReturnCreatedActor() {
    when(actorRepository.save(any(Actor.class))).thenReturn(actor);
    ActorDTO result = actorService.createActor(actorDTO);
    assertEquals(actorDTO, result);
  }

  @Test
  void updateActor_FullUpdate_ShouldReturnUpdatedActor() {
    ActorDTO fullUpdateDTO =
        ActorDTO.builder()
            .id(1L)
            .firstName("Bradley")
            .lastName("Pitt")
            .birthDate(LocalDate.of(1963, 12, 18))
            .nationality("American")
            .build();
    Actor updatedActor =
        Actor.builder()
            .id(1L)
            .firstName("Bradley")
            .lastName("Pitt")
            .birthDate(LocalDate.of(1963, 12, 18))
            .nationality("American")
            .build();
    when(actorRepository.findById(1L)).thenReturn(Optional.of(actor));
    when(actorRepository.save(any(Actor.class))).thenReturn(updatedActor);
    ActorDTO result = actorService.updateActor(1L, fullUpdateDTO);
    assertEquals(fullUpdateDTO, result);
  }

  @Test
  void updateActor_PartialUpdate_ShouldReturnPartiallyUpdatedActor() {
    ActorDTO partialUpdateDTO = ActorDTO.builder().firstName("Bradley").build();
    Actor updatedActor =
        Actor.builder()
            .id(1L)
            .firstName("Bradley")
            .lastName("Pitt")
            .birthDate(LocalDate.of(1963, 12, 18))
            .nationality("American")
            .build();
    ActorDTO updatedActorDTO =
        ActorDTO.builder()
            .id(1L)
            .firstName("Bradley")
            .lastName("Pitt")
            .birthDate(LocalDate.of(1963, 12, 18))
            .nationality("American")
            .build();
    when(actorRepository.findById(1L)).thenReturn(Optional.of(actor));
    when(actorRepository.save(any(Actor.class))).thenReturn(updatedActor);
    ActorDTO result = actorService.updateActor(1L, partialUpdateDTO);
    assertEquals(updatedActorDTO, result);
  }

  @Test
  void deleteActor_ShouldDeleteActor_WhenActorExists() {
    when(actorRepository.existsById(1L)).thenReturn(true);
    doNothing().when(actorRepository).deleteById(1L);
    actorService.deleteActor(1L);
    verify(actorRepository).deleteById(1L);
  }

  @Test
  void deleteActor_ShouldThrowException_WhenActorDoesNotExist() {
    when(actorRepository.existsById(99L)).thenReturn(false);
    assertThrows(EntityNotFoundException.class, () -> actorService.deleteActor(99L));
  }
}
