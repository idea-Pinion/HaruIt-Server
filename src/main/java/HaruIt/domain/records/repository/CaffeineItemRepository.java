package HaruIt.domain.records.repository;

import HaruIt.domain.records.entity.CaffeineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CaffeineItemRepository extends JpaRepository<CaffeineItem, Long> {
    List<CaffeineItem> findAllByUserIdIsNullOrUserIdOrderByIdAsc(Long userId);
    @Query("select i from CaffeineItem i where i.id = :id and (i.userId is null or i.userId = :userId)")
    Optional<CaffeineItem> findUsable(@Param("id") Long id, @Param("userId") Long userId);
    boolean existsByUserIdIsNull();
}
