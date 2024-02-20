package study.alarmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.alarmservice.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByEmail(String email);
}
