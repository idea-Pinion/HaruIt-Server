package HaruIt.domain.targets.dto;

import HaruIt.domain.targets.entity.Target;

import java.time.LocalDate;

public record TargetResponse(
        Long targetId,
        String title,
        LocalDate startDate,
        boolean checkedToday
) {
    public static TargetResponse from(Target t, boolean checkedToday) {
        return new TargetResponse(t.getId(), t.getTitle(), t.getStartDate(), checkedToday);
    }
}