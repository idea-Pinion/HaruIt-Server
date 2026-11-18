package HaruIt.domain.auth.Repository;

import HaruIt.domain.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<User,Long>{
        Optional<User> findByEmail(String email);

        boolean existsByEmail(String email);

}
