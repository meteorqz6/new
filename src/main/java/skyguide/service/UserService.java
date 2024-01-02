package skyguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skyguide.domain.User;
import skyguide.respository.UserRepository;

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

    public String findPassword(String email) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            return user.findPassword(email);
        } else {
            return null;
        }
    }

}
