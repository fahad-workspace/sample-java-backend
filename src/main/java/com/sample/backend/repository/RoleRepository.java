package com.sample.backend.repository;

import com.sample.backend.model.Role;
import jakarta.annotation.Nonnull;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  @Override
  @EntityGraph(attributePaths = {"movie", "actor"})
  @Nonnull
  List<Role> findAll();

  @EntityGraph(attributePaths = {"movie", "actor"})
  List<Role> findByMovieId(Long movieId);

  @EntityGraph(attributePaths = {"movie", "actor"})
  List<Role> findByActorId(Long actorId);

  @EntityGraph(attributePaths = {"movie", "actor"})
  List<Role> findByCharacterNameContainingIgnoreCase(String characterName);
}
