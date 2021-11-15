package com.spring.kkaemiGG.config.auth;

import com.spring.kkaemiGG.web.dto.user.UsersLoginResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) {
        UsersLoginResponseDto responseDto = UsersLoginResponseDto.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("LOGIN_FAILED")
                .build();
    }
}
