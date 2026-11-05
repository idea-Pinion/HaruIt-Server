package HaruIt.domain.report.dto;

public record Summary(
        Double avgSleepMinutes,
        int totalStudyMinutes,
        Double avgCondition,
        int totalCaffeineMg
) {}
