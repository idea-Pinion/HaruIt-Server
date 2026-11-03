package HaruIt.domain.records.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CaffeineItemRequest(
        @NotBlank @Size(max = 30) String name,
        @NotNull @Min(1) @Max(1000) Integer caffeineMg
) {}
