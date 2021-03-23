package com.spring.kkaemiGG.config.auth;

import com.google.gson.Gson;
import com.spring.kkaemiGG.web.dto.user.UsersLoginRequestDto;
import org.apache.http.entity.ContentType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final String contentType = ContentType.APPLICATION_JSON.withCharset("utf-8").toString();

    private UsersLoginRequestDto loginRequestDto;
    private boolean postOnly = true;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (postOnly && !request.getMethod().equalsIgnoreCase("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        // 클라이언트 요청 헤더가 json일 경우
        if (request.getHeader("Content-Type").equals(contentType)) {
            try {
                String json = request.getReader().lines().collect(Collectors.joining());
                loginRequestDto = new Gson().fromJson(json, UsersLoginRequestDto.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        username = username.trim();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {

        // 만약 Content-Type이 application/json이면 dto에서 패스워드 가져옴
        if (request.getHeader("Content-Type").equals(contentType)) {
            return loginRequestDto.getPassword();
        }

        return request.getParameter(super.getPasswordParameter());
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {

        // 만약 Content-Type이 application/json이면 dto에서 패스워드 가져옴
        if (request.getHeader("Content-Type").equals(contentType)) {
            return loginRequestDto.getEmail();
        }

        return request.getParameter(super.getUsernameParameter());
    }

    @Override
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }
}
