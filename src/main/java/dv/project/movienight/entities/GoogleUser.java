package dv.project.movienight.entities;



import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GoogleUser {
    @Id
    private String userId;

    private String email;

    private String accessToken;

    private String refreshToken;

    private Long expiresAt;

    public GoogleUser(){}

    public GoogleUser(String userId, String email, String accessToken, String refreshToken, Long expiresAt) {
        this.userId = userId;
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }
}
