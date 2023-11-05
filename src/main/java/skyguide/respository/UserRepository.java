package skyguide.respository;

import skyguide.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByIdentify(String identify);
    User findByRememberMeToken(String rememberMeToken);
}

