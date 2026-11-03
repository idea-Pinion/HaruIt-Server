package HaruIt.domain.records.controller;

import HaruIt.domain.records.dto.request.ConditionCreateRequest;
import HaruIt.domain.records.dto.request.ConditionUpdateRequest;
import HaruIt.domain.records.dto.response.ConditionResponse;
import HaruIt.domain.records.service.ConditionService;
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
@RequestMapping("/records/condition")
@RequiredArgsConstructor
public class ConditionController {
    private final ConditionService conditionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConditionResponse create(@AuthenticationPrincipal Long userId,
                                    @RequestBody @Valid ConditionCreateRequest req) {
        return conditionService.create(userId, req);
    }

    @GetMapping
    public List<ConditionResponse> getWeek(@AuthenticationPrincipal Long userId,
                                           @RequestParam @DateTimeFormat(iso = DATE)LocalDate weekStart) {
        return conditionService.getWeek(userId, weekStart);
    }

    @PatchMapping("/{recordId}")
    public ConditionResponse update(@AuthenticationPrincipal Long userId,
                                          @PathVariable Long recordId,
                                          @RequestBody @Valid ConditionUpdateRequest req) {
        return conditionService.update(userId, recordId, req);
    }

    @DeleteMapping("/{recordId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal Long userId,
                       @PathVariable Long recordId) {
        conditionService.delete(userId, recordId);
    }
}
