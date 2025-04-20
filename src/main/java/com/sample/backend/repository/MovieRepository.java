package com.sample.backend.repository;

import com.sample.backend.model.Genre;
import com.sample.backend.model.Movie;
import jakarta.annotation.Nonnull;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Movie entities. Provides methods for accessing and modifying movie data in the
 * database.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

  @Override
  @EntityGraph(attributePaths = {"director"})
  @Nonnull
  List<Movie> findAll();

  @EntityGraph(attributePaths = {"director"})
  List<Movie> findByTitleContainingIgnoreCaseAndGenre(String title, Genre genre);

  /** Finds movies by partial title match (case insensitive). */
  @EntityGraph(attributePaths = {"director"})
  List<Movie> findByTitleContainingIgnoreCase(String title);

  /** Finds movies by exact genre match. */
  @EntityGraph(attributePaths = {"director"})
  List<Movie> findByGenre(Genre genre);
}
