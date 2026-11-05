package HaruIt.domain.report.dto;

import java.time.LocalDate;

public record Day(
        LocalDate date,
        long sleepMinutes,
        int studyMinutes,
        Integer condition,
        int caffeineMg
) {}
