package HaruIt.domain.targets.entity;

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

import java.time.LocalDate;

@Entity
@Table(
        name = "target_check",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_target_check_target_date",
                columnNames = {"target_id", "check_date"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TargetCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long targetId;

    @Column(nullable = false)
    private LocalDate checkDate;

    public TargetCheck(Long targetId, LocalDate checkDate) {
        this.targetId = targetId;
        this.checkDate = checkDate;
    }
}
