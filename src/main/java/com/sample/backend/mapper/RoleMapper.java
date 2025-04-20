package com.sample.backend.mapper;

import com.sample.backend.dto.RoleDTO;
import com.sample.backend.model.Role;

public final class RoleMapper {

  public static RoleDTO toDTO(Role role) {
    if (role == null) {
      return null;
    }
    RoleDTO.RoleDTOBuilder builder =
        RoleDTO.builder().id(role.getId()).characterName(role.getCharacterName());
    if (role.getMovie() != null) {
      builder.movieId(role.getMovie().getId()).movieTitle(role.getMovie().getTitle());
    }
    if (role.getActor() != null) {
      builder
          .actorId(role.getActor().getId())
          .actorName(role.getActor().getFirstName() + " " + role.getActor().getLastName());
    }
    return builder.build();
  }

  // For partial updates
  public static void updateRoleFromDTO(Role role, RoleDTO dto) {
    if (dto.characterName() != null) {
      role.setCharacterName(dto.characterName());
    }
  }
}
