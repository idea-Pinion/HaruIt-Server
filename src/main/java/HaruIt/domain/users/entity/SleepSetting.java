package HaruIt.domain.users.entity;

import HaruIt.global.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.time.LocalTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SleepSetting {
    @Column(nullable = false)
    private LocalTime targetBedTime = LocalTime.of(23, 0);

    @Column(nullable = false)
    private LocalTime targetWakeTime = LocalTime.of(7,0);

    public void update(LocalTime bedTime, LocalTime wakeTime) {
        LocalTime newBed = bedTime != null ? bedTime : this.targetBedTime;
        LocalTime newWake = wakeTime != null ? wakeTime : this.targetWakeTime;
        if (newBed.equals(newWake)) {
            throw new CustomException(HttpStatus.NOT_FOUND, "취침 시각과 기상 시각이 같을 수 없습니다.");
        }
        this.targetBedTime = newBed;
        this.targetWakeTime = newWake;
    }

    public Duration targetSleepDuration() {
        Duration d = Duration.between(targetBedTime, targetWakeTime);
        return d.isNegative() ? d.plusDays(1) : d;
    }
}
