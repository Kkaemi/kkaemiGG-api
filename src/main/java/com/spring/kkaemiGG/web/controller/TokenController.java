package com.spring.kkaemiGG.web.controller;

import com.spring.kkaemiGG.annotation.CustomRestController;
import com.spring.kkaemiGG.auth.Token;
import com.spring.kkaemiGG.domain.token.RefreshToken;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.service.TokenService;
import com.spring.kkaemiGG.service.UserService;
import com.spring.kkaemiGG.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.spring.kkaemiGG.auth.Token.REFRESH_TOKEN_NAME;

@RequiredArgsConstructor
@CustomRestController
public class TokenController {

    private final TokenService tokenService;
    private final UserService userService;

    @GetMapping("/v1/token")
    public String refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        // refresh token id cookie 찾기
        // id 기반 refresh token entity 찾기
        Cookie refreshTokenIdCookie = CookieUtils.getCookie(request, REFRESH_TOKEN_NAME).orElse(null);
        if (refreshTokenIdCookie == null) return null;
        RefreshToken refreshTokenEntity = tokenService.findRefreshTokenById(Long.valueOf(refreshTokenIdCookie.getValue()));

        // entity 안에 있는 refresh token 검증
        if (!tokenService.verifyToken(refreshTokenEntity.getRefreshToken())) {
            CookieUtils.deleteCookie(request, response, REFRESH_TOKEN_NAME);
            tokenService.deleteRefreshToken(refreshTokenEntity);
            return null;
        }

        User user = userService.findById(tokenService.getUserId(refreshTokenEntity.getRefreshToken()));
        Token token = tokenService.generateToken(user);

        return token.getAccessToken();
    }

    @DeleteMapping("/v1/token")
    public void deleteTokenAndCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.getCookie(request, REFRESH_TOKEN_NAME)
                .map(cookie -> Long.parseLong(cookie.getValue()))
                .ifPresent(refreshTokenId -> {
                    tokenService.deleteRefreshToken(refreshTokenId);
                    CookieUtils.deleteCookie(request, response, REFRESH_TOKEN_NAME);
                });
    }
}
