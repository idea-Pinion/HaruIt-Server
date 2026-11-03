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

@Entity
@Getter
@Table(
        name = "caffeine_item",
        indexes = @Index(name = "idx_caffeine_item_user_id",
        columnList = "user_id")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CaffeineItem extends BaseTimeEntity {
    private static final int MAX_CAFFEINE_MG = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private int caffeineMg;

    private CaffeineItem(Long userId, String name, int caffeineMg) {
        validate(name, caffeineMg);
        this.userId = userId;
        this.name = name;
        this.caffeineMg = caffeineMg;
    }

    public static CaffeineItem preset(String name, int caffeineMg) {
        return new CaffeineItem(null, name, caffeineMg);
    }

    public static CaffeineItem custom(Long userId, String name, int caffeineMg) {
        if (userId == null) throw new IllegalArgumentException("userId는 필수입니다.");
        return new CaffeineItem(userId, name, caffeineMg);
    }

    public boolean isPreset() {
        return userId == null;
    }

    private static void validate(String name, int caffeineMg) {
        if (name == null || name.isBlank() || name.length() > 30)
            throw new CustomException(ErrorCode.INVALID_INPUT, "음료 이름은 1~30자여야 합니다.");
        if (caffeineMg <= 0 || caffeineMg > MAX_CAFFEINE_MG)
            throw new CustomException(ErrorCode.INVALID_INPUT, "카페인 함량은 1~1000mg 사이여야 합니다.");
    }
}
