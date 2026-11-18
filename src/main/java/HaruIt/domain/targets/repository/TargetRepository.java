package HaruIt.domain.targets.repository;

import HaruIt.domain.targets.entity.Target;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TargetRepository extends JpaRepository<Target, Long> {
    List<Target> findAllByUserIdOrderByIdDesc(Long userId);
    Optional<Target> findByIdAndUserId(Long id, Long userId);
    List<Target> findAllByUserIdAndStartDateLessThanEqualOrderByIdAsc(Long userId, LocalDate date);
}
