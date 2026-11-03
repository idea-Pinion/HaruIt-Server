package HaruIt.domain.records.repository;

import HaruIt.domain.records.entity.Sleep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SleepRepository extends JpaRepository<Sleep, Long> {
    Optional<Sleep> findByIdAndUserId(Long id, Long userId);

    List<Sleep> findAllByUserIdAndSleepDateBetweenOrderByBedTimeAsc(Long userId, LocalDate from, LocalDate to);

    @Query("""
        select case when count(r) > 0 then true else false end
        from Sleep r
        where r.userId = :userId
          and r.bedTime < :wakeTime and r.wakeTime > :bedTime
          and (:excludeId is null or r.id <> :excludeId)
        """)
    boolean existsOverlap(@Param("userId") Long userId,
                          @Param("bedTime") LocalDateTime bedTime,
                          @Param("wakeTime") LocalDateTime wakeTime,
                          @Param("excludeId") Long excludeId);
}
