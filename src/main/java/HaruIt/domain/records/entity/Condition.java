package HaruIt.domain.records.entity;

import HaruIt.global.entity.BaseTimeEntity;
import HaruIt.global.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@Entity
@Getter
@Table(
        name = "condition_record",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_condition_user_date",
                columnNames = {"user_id", "record_date"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Condition extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private LocalDate recordDate;

    // 1 ~ 5
    @Column(nullable = false)
    private int score;

    @Column(length = 200)
    private String memo;

    public Condition(Long userId, LocalDate recordDate, int score, String memo) {
        this.userId = userId;
        this.recordDate = recordDate;
        update(score, memo);
    }

    public void update(int score, String memo) {
        if (score < 1 || score > 5)
            throw new CustomException(HttpStatus.BAD_REQUEST, "컨디션 점수는 1~5 사이여야 합니다.");
        if (memo != null && memo.length() > 200)
            throw new CustomException(HttpStatus.BAD_REQUEST, "메모는 200자 이하여야 합니다.");
        this.score = score;
        this.memo = (memo == null || memo.isBlank()) ? null : memo;
    }
}
