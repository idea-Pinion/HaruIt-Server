package HaruIt.domain.report.dto;

import java.time.LocalDate;
import java.util.List;

public record WeeklyReportResponse(
        LocalDate weekStart,
        LocalDate weekEnd,
        Summary summary,
        List<Day> days
) {}