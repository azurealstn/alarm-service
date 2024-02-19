package study.alarmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.alarmservice.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
