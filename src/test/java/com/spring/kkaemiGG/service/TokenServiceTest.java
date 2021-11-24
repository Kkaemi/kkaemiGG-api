package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.auth.Token;
import com.spring.kkaemiGG.domain.token.RefreshTokenRepository;
import com.spring.kkaemiGG.domain.user.Role;
import com.spring.kkaemiGG.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class TokenServiceTest {

    private final TokenService tokenService = new TokenService(
            "6INawnEP4yQUgOJIzmnKowR+R1zJ+90xMrBqV2+E+YIH8rOcliXEVh8AX/f5WWamK8UsYX94qFVOqXqBqp2mng==",
            Mockito.mock(RefreshTokenRepository.class)
    );

    @DisplayName("토큰 생성 테스트")
    @Test
    public void generateToken() {
        System.out.println("given");
        User user = User.builder(
                        "kitys2k3@gmail.com",
                        "nickname",
                        Role.USER
                ).id(1L)
                .build();

        System.out.println("when");
        Token token = tokenService.generateToken(user);

        System.out.println(token.getAccessToken());
        System.out.println(token.getRefreshToken());

        System.out.println("then");
        assertThat(token.getAccessToken()).isNotNull();
        assertThat(token.getRefreshToken()).isNotNull();
    }

    @DisplayName("토큰 검증 테스트")
    @Test
    public void verifyToken() {
        User user = User.builder(
                        "kitys2k3@gmail.com",
                        "nickname",
                        Role.USER
                ).id(1L)
                .build();

        Token token = tokenService.generateToken(user);

        assertThat(tokenService.verifyToken(token.getAccessToken())).isEqualTo(true);
        assertThat(tokenService.verifyToken(token.getRefreshToken())).isEqualTo(true);
    }

    @DisplayName("토큰에서 유저 PK 꺼내기")
    @Test
    void getUserId() {
        User user = User.builder(
                        "kitys2k3@gmail.com",
                        "nickname",
                        Role.USER
                ).id(1L)
                .build();
        Token token = tokenService.generateToken(user);

        Long userId = tokenService.getUserId(token.getAccessToken());

        assertThat(userId).isEqualTo(1);
    }
}