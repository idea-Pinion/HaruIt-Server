package HaruIt.domain.users.dto;

import HaruIt.domain.users.entity.SleepSetting;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record SleepSettingDto(
        @JsonFormat(pattern = "HH:mm") LocalTime targetBedTime,
        @JsonFormat(pattern = "HH:mm") LocalTime targetWakeTime
) {
    public static SleepSettingDto from(SleepSetting s) {
        return new SleepSettingDto(s.getTargetBedTime(), s.getTargetWakeTime());
    }
}