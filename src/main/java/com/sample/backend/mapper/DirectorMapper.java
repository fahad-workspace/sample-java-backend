package com.sample.backend.mapper;

import com.sample.backend.dto.DirectorDTO;
import com.sample.backend.model.Director;

public final class DirectorMapper {

  public static DirectorDTO toDTO(Director director) {
    if (director == null) {
      return null;
    }
    return DirectorDTO.builder()
        .id(director.getId())
        .firstName(director.getFirstName())
        .lastName(director.getLastName())
        .birthDate(director.getBirthDate())
        .nationality(director.getNationality())
        .build();
  }

  public static Director toEntity(DirectorDTO dto) {
    if (dto == null) {
      return null;
    }
    return Director.builder()
        .firstName(dto.firstName())
        .lastName(dto.lastName())
        .birthDate(dto.birthDate())
        .nationality(dto.nationality())
        .build();
  }

  public static void updateDirectorFromDTO(Director director, DirectorDTO dto) {
    if (dto.firstName() != null) {
      director.setFirstName(dto.firstName());
    }
    if (dto.lastName() != null) {
      director.setLastName(dto.lastName());
    }
    if (dto.birthDate() != null) {
      director.setBirthDate(dto.birthDate());
    }
    if (dto.nationality() != null) {
      director.setNationality(dto.nationality());
    }
  }
}
