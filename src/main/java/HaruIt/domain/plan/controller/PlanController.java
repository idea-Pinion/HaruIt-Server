package HaruIt.domain.plan.controller;

import HaruIt.domain.plan.dto.SleepPlanResponse;
import HaruIt.domain.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;

    @GetMapping("/sleep")
    public SleepPlanResponse getSleepPlan(@AuthenticationPrincipal Long userId,
                                          @RequestParam(required = false) @DateTimeFormat(iso = DATE)
                                          LocalDate date) {
        return planService.getSleepPlan(userId, date);
    }
}
