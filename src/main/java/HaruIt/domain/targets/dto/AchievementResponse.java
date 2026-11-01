package HaruIt.domain.targets.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record AchievementResponse(
        Long targetId,
        LocalDate from,
        LocalDate to,
        long totalDays,
        long checkedDays,
        double rate
) {
    public static AchievementResponse of(Long targetId, LocalDate from, LocalDate to, long checkedDays) {
        long totalDays = ChronoUnit.DAYS.between(from, to) + 1;
        int rate = totalDays <= 0 ? 0 : (int) Math.round(checkedDays * 100.0 / totalDays);
        return new AchievementResponse(targetId, from, to, totalDays, checkedDays, rate);
    }
}