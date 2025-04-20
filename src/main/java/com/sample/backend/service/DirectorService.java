package com.sample.backend.service;

import com.sample.backend.dto.DirectorDTO;
import com.sample.backend.mapper.DirectorMapper;
import com.sample.backend.model.Director;
import com.sample.backend.repository.DirectorRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DirectorService extends BaseService<Director, Long> {

  private final DirectorRepository directorRepository;

  public DirectorService(DirectorRepository directorRepository) {
    super(directorRepository);
    this.directorRepository = directorRepository;
  }

  public List<DirectorDTO> getAllDirectors() {
    return directorRepository.findAll().stream()
        .map(DirectorMapper::toDTO)
        .collect(Collectors.toList());
  }

  public DirectorDTO getDirectorById(Long id) {
    return DirectorMapper.toDTO(findById(id));
  }

  public List<DirectorDTO> searchDirectors(String name) {
    return directorRepository
        .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name)
        .stream()
        .map(DirectorMapper::toDTO)
        .collect(Collectors.toList());
  }

  @Transactional
  public DirectorDTO createDirector(DirectorDTO directorDTO) {
    Director director = DirectorMapper.toEntity(directorDTO);
    Director savedDirector = directorRepository.save(director);
    return DirectorMapper.toDTO(savedDirector);
  }

  @Transactional
  public DirectorDTO updateDirector(Long id, DirectorDTO directorDTO) {
    // Full replacement if all fields are provided
    if ((directorDTO.firstName() != null)
        && (directorDTO.lastName() != null)
        && (directorDTO.birthDate() != null)
        && (directorDTO.nationality() != null)) {
      // Verify director exists
      findById(id);
      Director director = DirectorMapper.toEntity(directorDTO);
      director.setId(id); // Keep the same ID
      Director updatedDirector = directorRepository.save(director);
      return DirectorMapper.toDTO(updatedDirector);
    }
    Director director = findById(id);
    DirectorMapper.updateDirectorFromDTO(director, directorDTO);
    Director updatedDirector = directorRepository.save(director);
    return DirectorMapper.toDTO(updatedDirector);
  }

  public void deleteDirector(Long id) {
    deleteById(id);
  }
}
