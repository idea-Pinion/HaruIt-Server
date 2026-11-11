package HaruIt.domain.plan.service;

import HaruIt.domain.plan.dto.SleepPlanResponse;
import HaruIt.domain.records.service.CaffeineForecastService;
import HaruIt.domain.users.entity.NotificationSetting;
import HaruIt.domain.users.entity.SleepSetting;
import HaruIt.domain.users.entity.User;
import HaruIt.domain.users.repository.UserRepository;
import HaruIt.global.exception.CustomException;
import HaruIt.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {
    private static final int CAFFEINE_CUTOFF_HOURS = 6;

    private final UserRepository userRepository;
    private final CaffeineForecastService caffeineForecastService;
    private final Clock clock;

    public SleepPlanResponse getSleepPlan(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        SleepSetting sleep = user.getSleepSetting();
        NotificationSetting noti = user.getNotificationSetting();

        if (date == null) {
            LocalDateTime now = LocalDateTime.now(clock);
            boolean crossesMidnight = sleep.getTargetBedTime().isAfter(sleep.getTargetWakeTime());
            date = (crossesMidnight && now.toLocalTime().isBefore(sleep.getTargetWakeTime()))
                    ? now.toLocalDate().minusDays(1)
                    : now.toLocalDate();
        }

        LocalDateTime bedTime = date.atTime(sleep.getTargetBedTime());
        // 취침 시각이 기상 시각보다 이르면(예: 00:30 → 07:30) 자정 이후 취침이므로 다음 날로
        if (sleep.getTargetBedTime().isBefore(sleep.getTargetWakeTime()))
            bedTime = bedTime.plusDays(1);
        double caffeine = caffeineForecastService.residualAt(userId, bedTime);

        return new SleepPlanResponse(
                date,
                bedTime,
                bedTime.plus(sleep.targetSleepDuration()),
                sleep.targetSleepDuration().toMinutes(),
                noti.isBedtimeAlarm() ? bedTime.minusMinutes(noti.getBedtimeAlarmMinutesBefore()) : null,
                bedTime.minusHours(CAFFEINE_CUTOFF_HOURS),
                (int) Math.round(caffeine),
                CaffeineForecastService.isSleepSafe(caffeine)
        );
    }
}
