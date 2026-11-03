package HaruIt.domain.records.dto.response;

import HaruIt.domain.records.entity.Sleep;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SleepResponse(
        Long recordId,
        LocalDate sleepDate,
        LocalDateTime bedTime,
        LocalDateTime wakeTime,
        long sleepMinutes
) {
    public static SleepResponse from(Sleep r) {
        return new SleepResponse(r.getId(), r.getSleepDate(), r.getBedTime(),
                r.getWakeTime(), r.getSleepMinutes());
    }
}
