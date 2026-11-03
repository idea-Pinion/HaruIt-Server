package HaruIt.domain.records.repository;

import HaruIt.domain.records.entity.CaffeineIntake;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CaffeineIntakeRepository extends JpaRepository<CaffeineIntake, Long> {
    Optional<CaffeineIntake> findByIdAndUserId(Long id, Long userId);
    List<CaffeineIntake> findAllByUserIdAndIntakeAtGreaterThanEqualAndIntakeAtLessThanOrderByIntakeAtAsc(
            Long userId, LocalDateTime from, LocalDateTime to);
}
