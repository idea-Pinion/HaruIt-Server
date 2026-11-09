package HaruIt.domain.targets.repository;

import HaruIt.domain.targets.entity.TargetCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface TargetCheckRepository extends JpaRepository<TargetCheck, Long> {
    boolean existsByTargetIdAndCheckDate(Long targetId, LocalDate checkDate);

    long countByTargetIdAndCheckDateBetween(Long targetId, LocalDate from, LocalDate to);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from TargetCheck c where c.targetId = :targetId and c.checkDate = :date")
    void deleteCheck(@Param("targetId") Long targetId, @Param("date") LocalDate date);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from TargetCheck c where c.targetId = :targetId")
    void deleteAllByTargetId(@Param("targetId") Long targetId);

    @Query("select c.targetId from TargetCheck c where c.targetId in :targetIds and c.checkDate = :date")
    Set<Long> findCheckedTargetIds(@Param("targetIds") List<Long> targetIds, @Param("date") LocalDate date);

    @Modifying
    @Query(value = """
        insert into target_check (target_id, check_date)
        values (:targetId, :date)
        on duplicate key update target_id = target_id
        """, nativeQuery = true)
    void insertIfAbsent(@Param("targetId") Long targetId, @Param("date") LocalDate date);

    void deleteByTargetIdAndCheckDate(Long id, LocalDate date);
}