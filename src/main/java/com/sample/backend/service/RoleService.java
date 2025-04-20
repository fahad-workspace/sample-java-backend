package com.sample.backend.service;

import com.sample.backend.dto.RoleDTO;
import com.sample.backend.exception.EntityNotFoundException;
import com.sample.backend.mapper.RoleMapper;
import com.sample.backend.model.Actor;
import com.sample.backend.model.Movie;
import com.sample.backend.model.Role;
import com.sample.backend.repository.ActorRepository;
import com.sample.backend.repository.MovieRepository;
import com.sample.backend.repository.RoleRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleService extends BaseService<Role, Long> {

  private final RoleRepository roleRepository;
  private final MovieRepository movieRepository;
  private final ActorRepository actorRepository;

  public RoleService(
      RoleRepository roleRepository,
      MovieRepository movieRepository,
      ActorRepository actorRepository) {
    super(roleRepository);
    this.roleRepository = roleRepository;
    this.movieRepository = movieRepository;
    this.actorRepository = actorRepository;
  }

  public List<RoleDTO> getAllRoles() {
    return roleRepository.findAll().stream().map(RoleMapper::toDTO).collect(Collectors.toList());
  }

  public RoleDTO getRoleById(Long id) {
    return RoleMapper.toDTO(findById(id));
  }

  public List<RoleDTO> getRolesByMovieId(Long movieId) {
    return roleRepository.findByMovieId(movieId).stream()
        .map(RoleMapper::toDTO)
        .collect(Collectors.toList());
  }

  public List<RoleDTO> getRolesByActorId(Long actorId) {
    return roleRepository.findByActorId(actorId).stream()
        .map(RoleMapper::toDTO)
        .collect(Collectors.toList());
  }

  public List<RoleDTO> searchRolesByCharacterName(String characterName) {
    return roleRepository.findByCharacterNameContainingIgnoreCase(characterName).stream()
        .map(RoleMapper::toDTO)
        .collect(Collectors.toList());
  }

  @Transactional
  public RoleDTO createRole(RoleDTO roleDTO) {
    Role role = Role.builder().characterName(roleDTO.characterName()).build();
    if (roleDTO.movieId() != null) {
      Movie movie =
          movieRepository
              .findById(roleDTO.movieId())
              .orElseThrow(
                  () ->
                      new EntityNotFoundException("Movie not found with ID: " + roleDTO.movieId()));
      role.setMovie(movie);
    }
    if (roleDTO.actorId() != null) {
      Actor actor =
          actorRepository
              .findById(roleDTO.actorId())
              .orElseThrow(
                  () ->
                      new EntityNotFoundException("Actor not found with ID: " + roleDTO.actorId()));
      role.setActor(actor);
    }
    Role savedRole = roleRepository.save(role);
    return RoleMapper.toDTO(savedRole);
  }

  @Transactional
  public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
    Role role = findById(id);
    if (roleDTO.characterName() != null) {
      role.setCharacterName(roleDTO.characterName());
    }
    if (roleDTO.movieId() != null) {
      Movie movie =
          movieRepository
              .findById(roleDTO.movieId())
              .orElseThrow(
                  () ->
                      new EntityNotFoundException("Movie not found with ID: " + roleDTO.movieId()));
      role.setMovie(movie);
    }
    if (roleDTO.actorId() != null) {
      Actor actor =
          actorRepository
              .findById(roleDTO.actorId())
              .orElseThrow(
                  () ->
                      new EntityNotFoundException("Actor not found with ID: " + roleDTO.actorId()));
      role.setActor(actor);
    }
    Role updatedRole = roleRepository.save(role);
    return RoleMapper.toDTO(updatedRole);
  }

  public void deleteRole(Long id) {
    deleteById(id);
  }
}
