package skyguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skyguide.domain.User;
import skyguide.respository.UserRepository;;import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean isEmailAvailable(String email) {
        return userRepository.findByEmail(email) == null;
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // 자동로그인
    public User loginUserWithRememberMeToken(String rememberMeToken) {
        User user = userRepository.findByRememberMeToken(rememberMeToken);

        if (user != null) {
            return user;
        }
        return null;
    }

    public boolean checkUserExists(String email, String username, String phone) {
        Optional<User> userOptional = userRepository.findByUsernameAndEmailAndPhone(email, username, phone);
        return userOptional.isPresent();
    }

    public boolean resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }


}
