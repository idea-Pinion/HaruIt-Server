package HaruIt.domain.users.dto;

import HaruIt.domain.users.entity.NotificationSetting;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record NotificationSettingDto(
        Boolean bedtimeAlarm,
        @Min(5) @Max(120) Integer bedtimeAlarmMinutesBefore,
        Boolean caffeineAlarm,
        Boolean weeklyReportAlarm
) {
    public static NotificationSettingDto from(NotificationSetting n) {
        return new NotificationSettingDto(n.isBedtimeAlarm(), n.getBedtimeAlarmMinutesBefore(),
                n.isCaffeineAlarm(), n.isWeeklyReportAlarm());
    }
}