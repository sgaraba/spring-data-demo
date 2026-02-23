package md.utm2026.demo.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTagEntityDto(
        @NotBlank @Size(max = 100) String name
) {
}
