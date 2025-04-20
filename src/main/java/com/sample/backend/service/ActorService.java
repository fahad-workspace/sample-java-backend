package com.sample.backend.service;

import com.sample.backend.dto.ActorDTO;
import com.sample.backend.dto.PagedResponse;
import com.sample.backend.mapper.ActorMapper;
import com.sample.backend.model.Actor;
import com.sample.backend.repository.ActorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActorService extends BaseService<Actor, Long> {

  private final ActorRepository actorRepository;

  public ActorService(ActorRepository actorRepository) {
    super(actorRepository);
    this.actorRepository = actorRepository;
  }

  public PagedResponse<ActorDTO> getAllActors(int page, int size, String sort, String direction) {
    Direction sortDirection = Direction.fromOptionalString(direction).orElse(Direction.ASC);
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
    Page<ActorDTO> actorPage = actorRepository.findAll(pageable).map(ActorMapper::toDTO);
    return PagedResponse.from(actorPage);
  }

  public ActorDTO getActorById(Long id) {
    return ActorMapper.toDTO(findById(id));
  }

  public PagedResponse<ActorDTO> searchActors(String name, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<ActorDTO> actorPage =
        actorRepository
            .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name, pageable)
            .map(ActorMapper::toDTO);
    return PagedResponse.from(actorPage);
  }

  @Transactional
  public ActorDTO createActor(ActorDTO actorDTO) {
    Actor actor = ActorMapper.toEntity(actorDTO);
    Actor savedActor = actorRepository.save(actor);
    return ActorMapper.toDTO(savedActor);
  }

  @Transactional
  public ActorDTO updateActor(Long id, ActorDTO actorDTO) {
    if ((actorDTO.firstName() != null)
        && (actorDTO.lastName() != null)
        && (actorDTO.birthDate() != null)
        && (actorDTO.nationality() != null)) {
      findById(id);
      Actor actor = ActorMapper.toEntity(actorDTO);
      actor.setId(id);
      Actor updatedActor = actorRepository.save(actor);
      return ActorMapper.toDTO(updatedActor);
    }
    Actor actor = findById(id);
    ActorMapper.updateActorFromDTO(actor, actorDTO);
    Actor updatedActor = actorRepository.save(actor);
    return ActorMapper.toDTO(updatedActor);
  }

  @Transactional
  public void deleteActor(Long id) {
    deleteById(id);
  }
}
