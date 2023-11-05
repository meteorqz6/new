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

    public boolean isIdentifyAvailable(String identify) {
        return userRepository.findByIdentify(identify) == null;
    }

    public User loginUser(String identify, String password) {
        User user = userRepository.findByIdentify(identify);
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

    public String findPassword(String identify) {
        User user = userRepository.findByIdentify(identify);

        if (user != null) {
            return user.findPassword(identify);
        } else {
            return null;
        }
    }

}
