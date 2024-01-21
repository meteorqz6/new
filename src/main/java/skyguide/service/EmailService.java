package skyguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import skyguide.domain.User;
import skyguide.respository.UserRepository;

import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    public void sendVerificationEmail(String to, String verificationCode) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject("비밀번호 재설정을 위한 인증코드");
//        message.setText("인증코드: " + verificationCode + "\n 인증코드를 5분 내로 입력해 주세요.");
//
//        mailSender.send(message);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("비밀번호 재설정을 위한 인증코드");
            message.setText("인증코드: " + verificationCode + "\n 인증코드를 5분 내로 입력해 주세요.");

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace(); // 에러
        }
    }

    public boolean verifyUser(String username, String email, String phone, String verificationCode) {
        Optional<User> optionalUser = userRepository.findByUsernameAndEmailAndPhone(username, email, phone);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return verificationCode.equals(user.getVerificationCode());
        }
        return false;
    }

}
