package com.sample.backend.mapper;

import com.sample.backend.dto.ActorDTO;
import com.sample.backend.model.Actor;

public final class ActorMapper {

  public static ActorDTO toDTO(Actor actor) {
    if (actor == null) {
      return null;
    }
    return ActorDTO.builder()
        .id(actor.getId())
        .firstName(actor.getFirstName())
        .lastName(actor.getLastName())
        .birthDate(actor.getBirthDate())
        .nationality(actor.getNationality())
        .build();
  }

  public static Actor toEntity(ActorDTO dto) {
    if (dto == null) {
      return null;
    }
    return Actor.builder()
        .firstName(dto.firstName())
        .lastName(dto.lastName())
        .birthDate(dto.birthDate())
        .nationality(dto.nationality())
        .build();
  }

  public static void updateActorFromDTO(Actor actor, ActorDTO dto) {
    if (dto.firstName() != null) {
      actor.setFirstName(dto.firstName());
    }
    if (dto.lastName() != null) {
      actor.setLastName(dto.lastName());
    }
    if (dto.birthDate() != null) {
      actor.setBirthDate(dto.birthDate());
    }
    if (dto.nationality() != null) {
      actor.setNationality(dto.nationality());
    }
  }
}
