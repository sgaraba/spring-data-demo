package md.utm2026.demo.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTaskStatusEntityDto(
        @NotBlank @Size(max = 100) String name,
        @NotNull Boolean active
) {
}
