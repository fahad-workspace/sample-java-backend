package com.sample.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.List;

@Schema(description = "Paged response wrapper with navigation metadata")
public record PagedResponse<T>(
    @Schema(description = "Page content", requiredMode = RequiredMode.REQUIRED) List<T> content,
    @Schema(
            description = "Current page number (0-based)",
            example = "0",
            requiredMode = RequiredMode.REQUIRED)
        int page,
    @Schema(description = "Page size", example = "20", requiredMode = RequiredMode.REQUIRED)
        int size,
    @Schema(
            description = "Total number of elements",
            example = "42",
            requiredMode = RequiredMode.REQUIRED)
        long totalElements,
    @Schema(
            description = "Total number of pages",
            example = "3",
            requiredMode = RequiredMode.REQUIRED)
        int totalPages,
    @Schema(
            description = "Whether this is the first page",
            example = "true",
            requiredMode = RequiredMode.REQUIRED)
        boolean first,
    @Schema(
            description = "Whether this is the last page",
            example = "false",
            requiredMode = RequiredMode.REQUIRED)
        boolean last) {

  // Factory method to create from Spring Page
  public static <T> PagedResponse<T> from(org.springframework.data.domain.Page<T> page) {
    return new PagedResponse<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages(),
        page.isFirst(),
        page.isLast());
  }
}
