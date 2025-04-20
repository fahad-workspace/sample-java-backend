package com.sample.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.time.LocalDate;
import lombok.Builder;

@Builder
@Schema(description = "Director information")
public record DirectorDTO(
    @Schema(description = "Unique identifier for the director", example = "1") Long id,
    @Schema(
            description = "Director's first name",
            example = "Christopher",
            requiredMode = RequiredMode.REQUIRED)
        String firstName,
    @Schema(
            description = "Director's last name",
            example = "Nolan",
            requiredMode = RequiredMode.REQUIRED)
        String lastName,
    @Schema(
            description = "Director's birth date",
            example = "1970-07-30",
            requiredMode = RequiredMode.REQUIRED)
        LocalDate birthDate,
    @Schema(
            description = "Director's nationality",
            example = "British-American",
            requiredMode = RequiredMode.REQUIRED)
        String nationality) {}
