package com.sample.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String characterName;

  // Each role is associated with one movie
  @ManyToOne
  @JoinColumn(name = "movie_id")
  private Movie movie;

  // Each role is played by one actor
  @ManyToOne
  @JoinColumn(name = "actor_id")
  private Actor actor;
}
