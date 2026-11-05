package HaruIt.domain.report.controller;

import HaruIt.domain.report.dto.WeeklyReportResponse;
import HaruIt.domain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/weekly")
    public WeeklyReportResponse getWeekly(@AuthenticationPrincipal Long userId,
                                          @RequestParam(required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                          LocalDate weekStart) {
        return reportService.getWeekly(userId, weekStart);
    }
}
