package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.auth.Token;
import com.spring.kkaemiGG.config.AppProperties;
import com.spring.kkaemiGG.domain.token.RefreshTokenRepository;
import com.spring.kkaemiGG.domain.user.Role;
import com.spring.kkaemiGG.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private AppProperties appProperties;

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    private void beforeAll() {
        given(appProperties.getJwt()).willReturn(new AppProperties.Jwt(
                "ebIw4/tbxlwnySnZ+yINJl/sHohETxfzJkQ9Y0afz7/+ufc8Vh9GWWyNXfgZyma+gXsZi5eyhrHCwpx7a8KaxQ==",
                1800000,
                604800000
        ));
        tokenService.init();
    }

    @DisplayName("토큰 생성 테스트")
    @Test
    public void generateToken() {
        // given
        User user = User.builder(
                        "kitys2k3@gmail.com",
                        "nickname",
                        Role.USER
                )
                .id(1L).build();

        // when
        Token token = tokenService.generateToken(user);

        System.out.println(token.getAccessToken());
        System.out.println(token.getRefreshToken());

        // then
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