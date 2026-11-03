package HaruIt.domain.records.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SleepRequest(
        @NotNull LocalDateTime bedTime,
        @NotNull LocalDateTime wakeTime
) {}