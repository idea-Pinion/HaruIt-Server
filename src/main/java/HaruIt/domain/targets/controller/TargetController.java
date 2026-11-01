package HaruIt.domain.targets.controller;

import HaruIt.domain.targets.dto.AchievementResponse;
import HaruIt.domain.targets.dto.TargetRequest;
import HaruIt.domain.targets.dto.TargetResponse;
import HaruIt.domain.targets.service.TargetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequestMapping("/targets")
@RequiredArgsConstructor
public class TargetController {
    private final TargetService targetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TargetResponse create(@AuthenticationPrincipal Long userId,
                                 @RequestBody @Valid TargetRequest req) {
        return targetService.create(userId, req);
    }

    @GetMapping
    public List<TargetResponse> getAll(@AuthenticationPrincipal Long userId) {
        return targetService.getAll(userId);
    }

    @PatchMapping("/{targetId}")
    public TargetResponse update(@AuthenticationPrincipal Long userId,
                                 @PathVariable Long targetId,
                                 @RequestBody @Valid TargetRequest req) {
        return targetService.update(userId, targetId, req);
    }

    @DeleteMapping("/{targetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal Long userId,
                       @PathVariable Long targetId) {
        targetService.delete(userId, targetId);
    }

    @GetMapping("/{targetId}/achievement")
    public AchievementResponse achievement(@AuthenticationPrincipal Long userId,
                                           @PathVariable Long targetId,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate from,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate to) {
        return targetService.achievement(userId, targetId, from, to);
    }

    @PutMapping("/{targetId}/checks/{date}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void check(@AuthenticationPrincipal Long userId,
                      @PathVariable Long targetId,
                      @PathVariable @DateTimeFormat(iso = DATE) LocalDate date) {
        targetService.check(userId, targetId, date);
    }

    @DeleteMapping("/{targetId}/checks/{date}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void uncheck(@AuthenticationPrincipal Long userId,
                        @PathVariable Long targetId,
                        @PathVariable @DateTimeFormat(iso = DATE) LocalDate date) {
        targetService.uncheck(userId, targetId, date);
    }
}
