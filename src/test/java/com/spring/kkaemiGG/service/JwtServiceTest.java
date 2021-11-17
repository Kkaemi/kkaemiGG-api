package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.auth.Token;
import com.spring.kkaemiGG.domain.user.Role;
import com.spring.kkaemiGG.domain.user.User;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    JwtService jwtService;

    @Test
    public void generateToken() {
        User user = new User(
                "kitys2k3@gmail.com",
                "nickname",
                Role.USER
        );

        Token token = jwtService.generateToken(user);

        System.out.println(token.getAccessToken());
        System.out.println(token.getRefreshToken());

        assertThat(token.getAccessToken()).isNotNull();
        assertThat(token.getRefreshToken()).isNotNull();
    }

    @Test
    public void verifyToken() {
        User user = new User(
                "kitys2k3@gmail.com",
                "nickname",
                Role.USER
        );

        Token token = jwtService.generateToken(user);

        assertThat(jwtService.verifyToken(token.getAccessToken())).isEqualTo(true);
        assertThat(jwtService.verifyToken(token.getRefreshToken())).isEqualTo(true);
    }

}