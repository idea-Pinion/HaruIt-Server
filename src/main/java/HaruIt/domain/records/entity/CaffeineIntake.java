package HaruIt.domain.records.entity;

import HaruIt.global.entity.BaseTimeEntity;
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

import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        name = "caffeine_intake",
        indexes = @Index(name = "idx_caffeine_intake_user_at",
        columnList = "user_id, intake_at")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CaffeineIntake extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long itemId;

    @Column(nullable = false, length = 30)
    private String itemName;

    @Column(nullable = false)
    private int caffeineMg;

    @Column(nullable = false)
    private LocalDateTime intakeAt;

    public CaffeineIntake(Long userId, CaffeineItem item, LocalDateTime intakeAt) {
        this.userId = userId;
        change(item, intakeAt);
    }

    public void change(CaffeineItem item, LocalDateTime intakeAt) {
        this.itemId = item.getId();
        this.itemName = item.getName();
        this.caffeineMg = item.getCaffeineMg();
        this.intakeAt = intakeAt;
    }
}
