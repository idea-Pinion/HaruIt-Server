package HaruIt.domain.records.repository;

import HaruIt.domain.records.entity.Condition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
    Optional<Condition> findByIdAndUserId(Long id, Long userId);
    boolean existsByUserIdAndRecordDate(Long userId, LocalDate recordDate);
    List<Condition> findAllByUserIdAndRecordDateBetweenOrderByRecordDateAsc(Long userId, LocalDate from, LocalDate to);
}
