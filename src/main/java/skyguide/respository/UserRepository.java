package skyguide.respository;

import skyguide.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // login에서 사용
    User findByEmail(String email);
    User findByRememberMeToken(String rememberMeToken);

    // verifyUser, checkUserExists에서 사용
    Optional<User> findByUsernameAndEmailAndPhone(String username, String email, String phone);
}

