package HaruIt.domain.records.controller;

import HaruIt.domain.records.dto.request.SleepRequest;
import HaruIt.domain.records.dto.response.SleepResponse;
import HaruIt.domain.records.service.SleepService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequestMapping("/records/sleep")
@RequiredArgsConstructor
public class SleepController {
    private final SleepService sleepService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SleepResponse create(@AuthenticationPrincipal Long userId,
                                @RequestBody @Valid SleepRequest req) {
        return sleepService.create(userId, req);
    }

    @GetMapping
    public List<SleepResponse> getWeek(@AuthenticationPrincipal Long userId,
                                       @RequestParam @DateTimeFormat(iso = DATE) LocalDate weekStart) {
        return sleepService.getWeek(userId, weekStart);
    }

    @PatchMapping("/{recordId}")
    public SleepResponse update(@AuthenticationPrincipal Long userId,
                                      @PathVariable Long recordId,
                                      @RequestBody @Valid SleepRequest req) {
        return sleepService.update(userId, recordId, req);
    }

    @DeleteMapping("/{recordId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal Long userId,
                       @PathVariable Long recordId) {
        sleepService.delete(userId, recordId);
    }
}
