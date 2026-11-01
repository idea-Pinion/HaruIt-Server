package HaruIt.domain.users.repository;

import HaruIt.domain.users.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByToken(String token);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Device d where d.token = :token and d.user.id = :userId")
    int deleteByTokenAndUserId(@Param("token") String token, @Param("userId") Long userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Device d where d.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}
