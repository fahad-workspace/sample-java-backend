package com.sample.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
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

  public static <T> PagedResponse<T> from(Page<T> page) {
    return PagedResponse.<T>builder()
        .content(page.getContent())
        .page(page.getNumber())
        .size(page.getSize())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .first(page.isFirst())
        .last(page.isLast())
        .build();
  }
}
