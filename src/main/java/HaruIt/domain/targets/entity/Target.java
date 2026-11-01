package HaruIt.domain.targets.entity;

import HaruIt.global.entity.BaseTimeEntity;
import HaruIt.global.exception.CustomException;
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
@Table(name = "target", indexes = @Index(name = "idx_target_user_id", columnList = "user_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Target extends BaseTimeEntity {
    private static final int MAX_TITLE_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = MAX_TITLE_LENGTH)
    private String title;

    @Column(nullable = false, updatable = false)
    private LocalDate startDate;

    public Target(Long userId, String title, LocalDate startDate) {
        validateTitle(title);
        this.userId = userId;
        this.title = title;
        this.startDate = startDate;
    }

    public void rename(String title) {
        validateTitle(title);
        this.title = title;
    }

    public void validateCheckable(LocalDate date, LocalDate today) {
        if (date.isBefore(startDate) || date.isAfter(today))
            throw new CustomException(HttpStatus.BAD_REQUEST, "체크할 수 없는 날짜입니다.");
    }

    public void validateTitle(String title) {
        if (title == null || title.isBlank() || title.length() > MAX_TITLE_LENGTH)
            throw new CustomException(HttpStatus.BAD_REQUEST, "목표 제목은 1~50자여야 합니다.");
    }
}
