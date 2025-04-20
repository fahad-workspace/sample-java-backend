package com.sample.backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.BatchSize;

/**
 * Entity representing a movie. Contains basic movie information and maintains relationships with
 * director and roles.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Basic movie information
  private String title;

  @Enumerated(EnumType.STRING)
  private Genre genre; // Change from String to Genre

  private LocalDate releaseDate;
  private Integer durationMinutes;

  // Relationship with director (many movies can be directed by one director)
  @ManyToOne
  @JoinColumn(name = "director_id")
  private Director director;

  // Relationship with roles (a movie can have multiple roles)
  @Builder.Default
  @BatchSize(size = 500)
  @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Role> roles = new HashSet<>();
}
