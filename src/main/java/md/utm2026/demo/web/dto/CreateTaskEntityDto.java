package md.utm2026.demo.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTaskEntityDto(
        @NotBlank @Size(max = 255) String title,
        @NotBlank @Size(max = 1000) String description,
        @NotNull Long taskStatusId,
        Long assigneeId
) {
}
