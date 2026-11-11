package HaruIt.domain.plan.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SleepPlanResponse(
        LocalDate date,
        LocalDateTime bedTime,
        LocalDateTime wakeTime,
        long targetSleepMinutes,
        LocalDateTime winDownAt,
        LocalDateTime caffeineCutoffAt,
        int caffeineAtBedtimeMg,
        boolean caffeineSafe
) {
}
