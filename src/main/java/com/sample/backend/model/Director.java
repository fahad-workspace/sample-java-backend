package com.sample.backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Director {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String firstName;
  private String lastName;
  private LocalDate birthDate;
  private String nationality;

  @Builder.Default
  @BatchSize(size = 500)
  @OneToMany(mappedBy = "director", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Movie> movies = new HashSet<>();
}
