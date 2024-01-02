package skyguide.domain;

import jakarta.persistence.*;

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

//    이름, 이메일, 휴대폰번호가 모두 일치할 시로 수정
    public String findPassword(String email) {
        if (this.email.equals(email)) {
            return this.password;
        } else {
            return null;
        }
    }

}
