package skyguide.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    private String email;
    private String password;
    private String phone;

    private String rememberMeToken;
    private String verificationCode;
    private LocalDateTime verificationCodeTimeStamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRememberMeToken() {
        return rememberMeToken;
    }

    public String generateRememberMeToken() {
        this.rememberMeToken = UUID.randomUUID().toString(); // 랜덤한 UUID(Universally Unique Identifier) 생성
        return this.rememberMeToken;
    }

    public void setRememberMeToken(String rememberMeToken) {
        this.rememberMeToken = rememberMeToken;
        generateRememberMeToken();
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode){
        this.verificationCode = verificationCode;
        this.verificationCodeTimeStamp = LocalDateTime.now();
    }

    public LocalDateTime getVerificationCodeTimeStamp() {
        return verificationCodeTimeStamp;
    }

//    이름, 이메일, 휴대폰번호가 모두 일치할 시
//    public String findPassword(String username, String email, String phone) {
//        if (this.username.equals(username) && this.email.equals(email) && this.phone.equals(phone)) {
//            return this.password;
//        } else {
//            return null;
//        }
//    }

}
