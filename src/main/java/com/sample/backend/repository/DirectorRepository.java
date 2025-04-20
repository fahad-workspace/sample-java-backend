package com.sample.backend.repository;

import com.sample.backend.model.Director;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {

  List<Director> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
      String firstName, String lastName);
}
