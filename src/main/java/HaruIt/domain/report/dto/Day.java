package HaruIt.domain.report.dto;

import java.time.LocalDate;

public record Day(
        LocalDate date,
        Integer sleepMinutes,
        int studyMinutes,
        Integer condition,
        int caffeineMg
) {}
