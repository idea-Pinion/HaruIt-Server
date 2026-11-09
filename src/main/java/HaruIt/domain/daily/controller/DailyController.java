package HaruIt.domain.daily.controller;

import HaruIt.domain.daily.dto.DailyResponse;
import HaruIt.domain.daily.dto.DailyUpdateRequest;
import HaruIt.domain.daily.service.DailyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequestMapping("/daily")
@RequiredArgsConstructor
public class DailyController {
    private final DailyService dailyService;

    @GetMapping("/{date}")
    public DailyResponse get(@AuthenticationPrincipal Long userId,
                             @PathVariable @DateTimeFormat(iso = DATE) LocalDate date) {
        return dailyService.get(userId, date);
    }

    @PatchMapping("/{date}")
    public DailyResponse update(@AuthenticationPrincipal Long userId,
                                @PathVariable @DateTimeFormat(iso = DATE) LocalDate date,
                                @RequestBody @Valid DailyUpdateRequest req) {
        return dailyService.update(userId, date, req);
    }
}
