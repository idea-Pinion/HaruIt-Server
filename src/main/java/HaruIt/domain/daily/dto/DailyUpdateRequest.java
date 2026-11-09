package HaruIt.domain.daily.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DailyUpdateRequest(
        @Valid ConditionPart condition,
        List<@Valid @NotNull TargetCheckPart> targets
) {
    public record ConditionPart(
            @NotNull @Min(1) @Max(5) Integer score,
            @Size(max = 200) String memo
    ) {}

    public record TargetCheckPart(
            @NotNull Long targetId,
            @NotNull Boolean checked
    ) {}
}
