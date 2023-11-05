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
    private String identify;
    private String password;

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

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRememberMeToken() {
        return rememberMeToken;
    }

    public String generateRememberMeToken() {
        return UUID.randomUUID().toString(); // 랜덤한 UUID(Universally Unique Identifier) 생성
    }

    public void setRememberMeToken(String rememberMeToken) {
        this.rememberMeToken = generateRememberMeToken();
    }

    public String findPassword(String email) {
        if (this.identify.equals(email)) {
            return this.password;
        } else {
            return null;
        }
    }

}
