package com.spring.kkaemiGG.auth;

import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.domain.user.UserRepository;
import com.spring.kkaemiGG.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        getToken((HttpServletRequest) request)
                .ifPresent(token -> {
                    // 만약 토큰이 있을 경우 토큰 검사
                    // 토큰이 유효하면 SecurityContext 에 User 객체를 넣어준다.
                    if (jwtService.verifyToken(token)) {
                        Long userId = jwtService.getUserId(token);

                        User user = userRepository.getOne(userId);

                        SecurityContextHolder.getContext().setAuthentication(
                                new UsernamePasswordAuthenticationToken(
                                        user,
                                        null,
                                        Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getKey()))
                                )
                        );
                    }
                });

        chain.doFilter(request, response);
    }

    private Optional<String> getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.isEmpty(bearerToken) || !bearerToken.startsWith("Bearer")) {
            return Optional.empty();
        }

        return Optional.of(bearerToken.substring(7));
    }
}
