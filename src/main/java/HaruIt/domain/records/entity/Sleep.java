package HaruIt.domain.records.entity;

import HaruIt.global.entity.BaseTimeEntity;
import HaruIt.global.exception.CustomException;
import HaruIt.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        name = "sleep_record",
        indexes = @Index(name = "idx_sleep_user_date",
        columnList = "user_id, sleep_date")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sleep extends BaseTimeEntity {
    private static final Duration MAX_DURATION = Duration.ofHours(24);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime bedTime;

    @Column(nullable = false)
    private LocalDateTime wakeTime;

    @Column(nullable = false)
    private LocalDate sleepDate;

    public Sleep(Long userId, LocalDateTime bedTime, LocalDateTime wakeTime) {
        this.userId = userId;
        update(bedTime, wakeTime);
    }

    public void update(LocalDateTime bedTime, LocalDateTime wakeTime) {
        Duration d = Duration.between(bedTime, wakeTime);
        if (d.isNegative() || d.isZero() || d.compareTo(MAX_DURATION) > 0)
            throw new CustomException(ErrorCode.INVALID_SLEEP_RANGE);
        this.bedTime = bedTime;
        this.wakeTime = wakeTime;
        this.sleepDate = wakeTime.toLocalDate();
    }

    public long getSleepMinutes() {
        return Duration.between(bedTime, wakeTime).toMinutes();
    }
}
