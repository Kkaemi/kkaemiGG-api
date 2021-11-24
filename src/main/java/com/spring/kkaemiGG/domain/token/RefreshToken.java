package com.spring.kkaemiGG.domain.token;

import com.spring.kkaemiGG.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REFRESH_TOKEN_ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(nullable = false, length = 500)
    private String refreshToken;

    public static RefreshTokenBuilder builder(
            User user,
            String refreshToken
    ) {
        Assert.notNull(user, "User must not be null");
        Assert.hasText(refreshToken, "Refresh Token must not be null");

        return new RefreshTokenBuilder()
                .user(user)
                .refreshToken(refreshToken);
    }

    public void updateToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
