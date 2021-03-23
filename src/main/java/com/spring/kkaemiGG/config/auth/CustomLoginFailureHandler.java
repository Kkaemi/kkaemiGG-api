package com.spring.kkaemiGG.config.auth;

import com.google.gson.Gson;
import com.spring.kkaemiGG.web.dto.user.UsersLoginResponseDto;
import org.apache.commons.codec.CharEncoding;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        UsersLoginResponseDto responseDto = UsersLoginResponseDto.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("LOGIN_FAILED")
                .build();

        PrintWriter out = response.getWriter();
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setStatus(HttpStatus.OK.value());
        out.print(new Gson().toJson(responseDto));
        out.flush();

    }
}
