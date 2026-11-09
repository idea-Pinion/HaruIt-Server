package HaruIt.domain.daily.dto;

import HaruIt.domain.records.entity.CaffeineIntake;
import HaruIt.domain.records.entity.Condition;
import HaruIt.domain.records.entity.Sleep;
import HaruIt.domain.records.entity.Study;
import HaruIt.domain.targets.entity.Target;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record DailyResponse(
        LocalDate date,
        Summary summary,
        List<SleepItem> sleeps,
        List<StudyItem> studies,
        ConditionItem condition,
        List<CaffeineIntakeItem> caffeines,
        List<TargetItem> targets
) {
    public record Summary(
            long sleepMinutes,
            int studyMinutes,
            int caffeineMg,
            int checkedTargets,
            int totalTargets
    ) {}

    public record SleepItem(
            Long id,
            LocalDateTime bedTime,
            LocalDateTime wakeTime,
            long minutes
    ) {}

    public record StudyItem(
            Long id,
            String subject,
            int minutes
    ) {}

    public record ConditionItem(
            Long id,
            int score,
            String memo
    ) {}

    public record CaffeineIntakeItem(
            Long id,
            Long itemId,
            String itemName,
            int caffeineMg,
            LocalDateTime intakeAt
    ) {}

    public record TargetItem(
            Long id,
            String title,
            boolean checked
    ) {}

    public static DailyResponse of(LocalDate date, List<Sleep> sleeps, List<Study> studies, Condition condition,
                                   List<CaffeineIntake> caffeines, List<Target> targets, Set<Long> checkedIds) {
        List<TargetItem> targetItems = targets.stream()
                .map(t -> new TargetItem(t.getId(), t.getTitle(), checkedIds.contains(t.getId())))
                .toList();

        Summary summary = new Summary(
                sleeps.stream().mapToLong(Sleep::getSleepMinutes).sum(),
                studies.stream().mapToInt(Study::getMinutes).sum(),
                caffeines.stream().mapToInt(CaffeineIntake::getCaffeineMg).sum(),
                checkedIds.size(),
                targets.size()
        );

        return new DailyResponse(
                date,
                summary,
                sleeps.stream().map(s -> new SleepItem(s.getId(), s.getBedTime(), s.getWakeTime(), s.getSleepMinutes())).toList(),
                studies.stream().map(s -> new StudyItem(s.getId(), s.getSubject(), s.getMinutes())).toList(),
                condition == null ? null : new ConditionItem(condition.getId(), condition.getScore(), condition.getMemo()),
                caffeines.stream().map(c -> new CaffeineIntakeItem(c.getId(), c.getItemId(), c.getItemName(), c.getCaffeineMg(), c.getIntakeAt())).toList(),
                targetItems
        );
    }
}
