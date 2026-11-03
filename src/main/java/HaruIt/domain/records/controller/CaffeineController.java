package HaruIt.domain.records.controller;

import HaruIt.domain.records.dto.request.CaffeineIntakeRequest;
import HaruIt.domain.records.dto.request.CaffeineItemRequest;
import HaruIt.domain.records.dto.response.CaffeineForecastResponse;
import HaruIt.domain.records.dto.response.CaffeineIntakeResponse;
import HaruIt.domain.records.dto.response.CaffeineItemResponse;
import HaruIt.domain.records.service.CaffeineForecastService;
import HaruIt.domain.records.service.CaffeineService;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@RestController
@RequestMapping("/records/caffeine")
@RequiredArgsConstructor
public class CaffeineController {
    private final CaffeineService caffeineService;
    private final CaffeineForecastService forecastService;

    // 음료 항목

    @GetMapping("/items")
    public List<CaffeineItemResponse> getItems(@AuthenticationPrincipal Long userId) {
        return caffeineService.getItems(userId);
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CaffeineItemResponse addItem(@AuthenticationPrincipal Long userId,
                                        @RequestBody @Valid CaffeineItemRequest req) {
        return caffeineService.addItem(userId, req);
    }

    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@AuthenticationPrincipal Long userId,
                           @PathVariable Long itemId) {
        caffeineService.deleteItem(userId, itemId);
    }

    // 섭취 기록

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CaffeineIntakeResponse create(@AuthenticationPrincipal Long userId,
                                         @RequestBody @Valid CaffeineIntakeRequest req) {
        return caffeineService.create(userId, req);
    }

    @GetMapping
    public List<CaffeineIntakeResponse> getWeek(@AuthenticationPrincipal Long userId,
                                                @RequestParam @DateTimeFormat(iso = DATE) LocalDate weekStart) {
        return caffeineService.getWeek(userId, weekStart);
    }

    @PatchMapping("/{recordId}")
    public CaffeineIntakeResponse update(@AuthenticationPrincipal Long userId,
                                         @PathVariable Long recordId,
                                         @RequestBody @Valid CaffeineIntakeRequest req) {
        return caffeineService.update(userId, recordId, req);
    }

    @DeleteMapping("/{recordId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal Long userId,
                       @PathVariable Long recordId) {
        caffeineService.delete(userId, recordId);
    }

    // 잔류량 예측

    @GetMapping("/forecast")
    public CaffeineForecastResponse forecast(@AuthenticationPrincipal Long userId,
                                             @RequestParam Long itemId,
                                             @RequestParam(required = false) @DateTimeFormat(iso = DATE_TIME) LocalDateTime at) {
        return forecastService.forecast(userId, itemId, at);
    }
}
