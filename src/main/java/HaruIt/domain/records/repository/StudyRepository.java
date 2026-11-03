package HaruIt.domain.records.repository;

import HaruIt.domain.records.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {
    Optional<Study> findByIdAndUserId(Long id, Long userId);
    List<Study> findAllByUserIdAndStudyDateBetweenOrderByStudyDateAscIdAsc(Long userId, LocalDate from, LocalDate to);
}
