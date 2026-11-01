package HaruIt.domain.users.entity;

import HaruIt.global.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Embeddable
public class NotificationSetting {
    private static final int MIN_MINUTES_BEFORE = 5;
    private static final int MAX_MINUTES_BEFORE = 120;

    @Column(nullable = false) private boolean bedtimeAlarm = true;
    @Column(nullable = false) private int bedtimeAlarmMinutesBefore = 30;
    @Column(nullable = false) private boolean caffeineAlarm = true;
    @Column(nullable = false) private boolean weeklyReportAlarm = true;

    public void update(Boolean bedtimeAlarm, Integer minutesBefore,
                       Boolean caffeineAlarm, Boolean weeklyReportAlarm) {
        if (minutesBefore != null &&
                (minutesBefore < MIN_MINUTES_BEFORE || minutesBefore > MAX_MINUTES_BEFORE)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "알림 시간은 5~120분 사이여야 합니다.");
        }
        if (bedtimeAlarm != null) this.bedtimeAlarm = bedtimeAlarm;
        if (minutesBefore != null) this.bedtimeAlarmMinutesBefore = minutesBefore;
        if (caffeineAlarm != null) this.caffeineAlarm = caffeineAlarm;
        if (weeklyReportAlarm != null) this.weeklyReportAlarm = weeklyReportAlarm;
    }
}
