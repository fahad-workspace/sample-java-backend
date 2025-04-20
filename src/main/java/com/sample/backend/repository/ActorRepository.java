package com.sample.backend.repository;

import com.sample.backend.model.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

  Page<Actor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
      String firstName, String lastName, Pageable pageable);
}
