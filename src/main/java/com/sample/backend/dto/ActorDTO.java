package com.sample.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.time.LocalDate;
import lombok.Builder;

@Builder
@Schema(description = "Actor information")
public record ActorDTO(
    @Schema(description = "Unique identifier for the actor", example = "1") Long id,
    @Schema(
            description = "Actor's first name",
            example = "Leonardo",
            requiredMode = RequiredMode.REQUIRED)
        String firstName,
    @Schema(
            description = "Actor's last name",
            example = "DiCaprio",
            requiredMode = RequiredMode.REQUIRED)
        String lastName,
    @Schema(
            description = "Actor's birth date",
            example = "1974-11-11",
            requiredMode = RequiredMode.REQUIRED)
        LocalDate birthDate,
    @Schema(
            description = "Actor's nationality",
            example = "American",
            requiredMode = RequiredMode.REQUIRED)
        String nationality) {}
