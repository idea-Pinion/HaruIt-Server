package HaruIt.domain.records.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CaffeineIntakeRequest(
        @NotNull Long itemId,
        @NotNull LocalDateTime intakeAt
) {}
