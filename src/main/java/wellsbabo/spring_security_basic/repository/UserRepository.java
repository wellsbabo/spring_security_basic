package wellsbabo.spring_security_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wellsbabo.spring_security_basic.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

}
