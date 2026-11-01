package HaruIt.domain.targets.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TargetRequest(
        @NotBlank
        @Size(max = 50)
        String title
) {}