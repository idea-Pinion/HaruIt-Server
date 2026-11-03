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

import java.time.LocalDate;

@Entity
@Getter
@Table(
        name = "study",
        indexes = @Index(name = "idx_study_user_date",
        columnList = "user_id, study_date")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study extends BaseTimeEntity {
    private static final int MAX_SUBJECT_LENGTH = 30;
    private static final int MAX_MINUTES = 24 * 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate studyDate;

    @Column(nullable = false, length = 30)
    private String subject;

    @Column(nullable = false)
    private int minutes;

    public Study(Long userId, LocalDate studyDate, String subject, int minutes) {
        this.userId = userId;
        update(studyDate, subject, minutes);
    }

    public void update(LocalDate studyDate, String subject, int minutes) {
        if (subject == null || subject.isBlank() || subject.length() > MAX_SUBJECT_LENGTH)
            throw new CustomException(ErrorCode.INVALID_INPUT, "과목은 1~30자여야 합니다.");
        if (minutes <= 0 || minutes > MAX_MINUTES)
            throw new CustomException(ErrorCode.INVALID_INPUT, "학습 시간은 1~1440분이어야 합니다.");
        this.studyDate = studyDate;
        this.subject = subject;
        this.minutes = minutes;
    }
}
