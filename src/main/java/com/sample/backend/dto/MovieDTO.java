package com.sample.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.time.LocalDate;
import lombok.Builder;

/**
 * Data Transfer Object for Movie entities. Provides a clean representation of movie data for API
 * responses and requests.
 */
@Builder
@Schema(description = "Movie information")
public record MovieDTO(
    @Schema(description = "Unique identifier for the movie", example = "1") Long id,
    @Schema(
            description = "Movie title",
            example = "The Shawshank Redemption",
            requiredMode = RequiredMode.REQUIRED)
        String title,
    @Schema(description = "Movie genre", example = "Drama", requiredMode = RequiredMode.REQUIRED)
        String genre,
    @Schema(
            description = "Release date",
            example = "1994-09-23",
            requiredMode = RequiredMode.REQUIRED)
        LocalDate releaseDate,
    @Schema(
            description = "Duration in minutes",
            example = "142",
            requiredMode = RequiredMode.REQUIRED)
        Integer durationMinutes,
    @Schema(description = "Director's ID", example = "1") Long directorId,
    @Schema(description = "Director's full name", example = "Frank Darabont")
        String directorName) {}
