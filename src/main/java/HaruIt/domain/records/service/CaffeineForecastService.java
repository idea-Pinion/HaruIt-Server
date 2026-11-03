package HaruIt.domain.records.service;

import HaruIt.domain.records.dto.response.CaffeineForecastResponse;
import HaruIt.domain.records.entity.CaffeineIntake;
import HaruIt.domain.records.entity.CaffeineItem;
import HaruIt.domain.records.repository.CaffeineIntakeRepository;
import HaruIt.domain.users.entity.SleepSetting;
import HaruIt.domain.users.repository.UserRepository;
import HaruIt.global.exception.CustomException;
import HaruIt.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CaffeineForecastService {
    private static final double HALF_LIFE_HOURS = 5.0;
    private static final int SLEEP_SAFE_MG = 50;

    private final CaffeineService caffeineService;
    private final CaffeineIntakeRepository intakeRepository;
    private final UserRepository userRepository;
    private final Clock clock;

    public CaffeineForecastResponse forecast(Long userId, Long itemId, LocalDateTime at) {
        LocalDateTime base = at != null ? at : LocalDateTime.now(clock);
        CaffeineItem item = caffeineService.getItem(userId, itemId);
        LocalDateTime bedtime = nextBedTime(userId, base);

        // 24시간 이전 섭취분은 반감기 5시간 기준 4% 미만이라 무시
        List<CaffeineIntake> intakes = intakeRepository
                .findAllByUserIdAndIntakeAtGreaterThanEqualAndIntakeAtLessThanOrderByIntakeAtAsc(
                        userId, bedtime.minusDays(1), bedtime);

        double current = intakes.stream()
                .mapToDouble(i -> residual(i.getCaffeineMg(), i.getIntakeAt(), bedtime))
                .sum();
        double ifDrink = current + residual(item.getCaffeineMg(), base, bedtime);

        return new CaffeineForecastResponse(
                itemId, base, bedtime,
                (int) Math.round(current), (int) Math.round(ifDrink),
                ifDrink < SLEEP_SAFE_MG
        );
    }

    private LocalDateTime nextBedTime(Long userId, LocalDateTime base) {
        SleepSetting s = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
                .getSleepSetting();
        LocalTime bed = s.getTargetBedTime();
        LocalTime wake = s.getTargetWakeTime();
        LocalTime now = base.toLocalTime();

        // 23:00~07:00처럼 자정을 넘기는 구간과 01:00~08:00처럼 안 넘기는 구간 모두 처리
        boolean inSleepWindow = bed.isAfter(wake)
                ? !now.isBefore(bed) || now.isBefore(wake)
                : !now.isBefore(bed) && now.isBefore(wake);
        if (inSleepWindow) return base;

        LocalDateTime next = base.toLocalDate().atTime(bed);
        return next.isBefore(base) ? next.plusDays(1) : next;
    }

    private double residual(int mg, LocalDateTime intakeAt, LocalDateTime target) {
        double hours = Duration.between(intakeAt, target).toMinutes() / 60.0;
        if (hours < 0) return 0;
        return mg * Math.pow(0.5, hours / HALF_LIFE_HOURS);
    }
}
