package HaruIt.domain.records.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ConditionCreateRequest(
        @NotNull LocalDate recordDate,
        @NotNull @Min(1) @Max(5) Integer score,
        @Size(max = 200) String memo
) {}
