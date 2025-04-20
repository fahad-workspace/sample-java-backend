package com.sample.backend.config;

import com.sample.backend.model.Actor;
import com.sample.backend.model.Director;
import com.sample.backend.model.Genre;
import com.sample.backend.model.Movie;
import com.sample.backend.model.Role;
import com.sample.backend.repository.ActorRepository;
import com.sample.backend.repository.DirectorRepository;
import com.sample.backend.repository.MovieRepository;
import com.sample.backend.repository.RoleRepository;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

  private final DirectorRepository directorRepository;
  private final ActorRepository actorRepository;
  private final MovieRepository movieRepository;
  private final RoleRepository roleRepository;

  public DataInitializer(
      DirectorRepository directorRepository,
      ActorRepository actorRepository,
      MovieRepository movieRepository,
      RoleRepository roleRepository) {
    this.directorRepository = directorRepository;
    this.actorRepository = actorRepository;
    this.movieRepository = movieRepository;
    this.roleRepository = roleRepository;
  }

  @Override
  public void run(String... args) {
    // Create directors using Lombok builders
    Director christopherNolan =
        Director.builder()
            .firstName("Christopher")
            .lastName("Nolan")
            .birthDate(LocalDate.of(1970, 7, 30))
            .nationality("British-American")
            .build();
    directorRepository.save(christopherNolan);
    Director quentinTarantino =
        Director.builder()
            .firstName("Quentin")
            .lastName("Tarantino")
            .birthDate(LocalDate.of(1963, 3, 27))
            .nationality("American")
            .build();
    directorRepository.save(quentinTarantino);
    // Create actors using Lombok builders
    Actor leonardoDiCaprio =
        Actor.builder()
            .firstName("Leonardo")
            .lastName("DiCaprio")
            .birthDate(LocalDate.of(1974, 11, 11))
            .nationality("American")
            .build();
    actorRepository.save(leonardoDiCaprio);
    Actor christianBale =
        Actor.builder()
            .firstName("Christian")
            .lastName("Bale")
            .birthDate(LocalDate.of(1974, 1, 30))
            .nationality("British")
            .build();
    actorRepository.save(christianBale);
    Actor samuelJackson =
        Actor.builder()
            .firstName("Samuel L.")
            .lastName("Jackson")
            .birthDate(LocalDate.of(1948, 12, 21))
            .nationality("American")
            .build();
    actorRepository.save(samuelJackson);
    // Create movies using Lombok builders
    Movie inception =
        Movie.builder()
            .title("Inception")
            .genre(Genre.SCI_FI)
            .releaseDate(LocalDate.of(2010, 7, 16))
            .durationMinutes(148)
            .director(christopherNolan)
            .build();
    movieRepository.save(inception);
    Movie darkKnight =
        Movie.builder()
            .title("The Dark Knight")
            .genre(Genre.ACTION)
            .releaseDate(LocalDate.of(2008, 7, 18))
            .durationMinutes(152)
            .director(christopherNolan)
            .build();
    movieRepository.save(darkKnight);
    Movie pulpFiction =
        Movie.builder()
            .title("Pulp Fiction")
            .genre(Genre.CRIME)
            .releaseDate(LocalDate.of(1994, 10, 14))
            .durationMinutes(154)
            .director(quentinTarantino)
            .build();
    movieRepository.save(pulpFiction);
    // Create roles using Lombok builders
    Role cobb =
        Role.builder().characterName("Dom Cobb").movie(inception).actor(leonardoDiCaprio).build();
    roleRepository.save(cobb);
    Role batman =
        Role.builder()
            .characterName("Bruce Wayne / Batman")
            .movie(darkKnight)
            .actor(christianBale)
            .build();
    roleRepository.save(batman);
    Role jules =
        Role.builder()
            .characterName("Jules Winnfield")
            .movie(pulpFiction)
            .actor(samuelJackson)
            .build();
    roleRepository.save(jules);
  }
}
