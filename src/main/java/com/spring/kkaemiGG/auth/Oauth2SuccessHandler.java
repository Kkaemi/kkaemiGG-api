package com.spring.kkaemiGG.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.common.contenttype.ContentType;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.domain.user.UserRepository;
import com.spring.kkaemiGG.exception.InternalServerErrorException;
import com.spring.kkaemiGG.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
@Component
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @SneakyThrows(InternalServerErrorException.class)
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = userRepository.findByEmail(oAuth2User.getAttribute("email"))
                .orElseThrow(() -> new InternalServerErrorException("해당 email의 유저를 찾을 수 없습니다."));

        Token token = jwtService.generateToken(user);

        responseJwt(response, token);
    }

    private void responseJwt(HttpServletResponse response, Token token) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        PrintWriter writer = response.getWriter();
        writer.print(objectMapper.writeValueAsString(token));
        writer.flush();
    }
}
