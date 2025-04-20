package com.sample.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Builder;

@Builder
@Schema(description = "Role information")
public record RoleDTO(
    @Schema(description = "Unique identifier for the role", example = "1") Long id,
    @Schema(
            description = "Character name in the movie",
            example = "Dom Cobb",
            requiredMode = RequiredMode.REQUIRED)
        String characterName,
    @Schema(description = "Movie ID", example = "1", requiredMode = RequiredMode.REQUIRED)
        Long movieId,
    @Schema(description = "Movie title", example = "Inception") String movieTitle,
    @Schema(description = "Actor ID", example = "1", requiredMode = RequiredMode.REQUIRED)
        Long actorId,
    @Schema(description = "Actor name", example = "Leonardo DiCaprio") String actorName) {}
