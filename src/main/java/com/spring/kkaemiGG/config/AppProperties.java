package com.spring.kkaemiGG.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.crypto.SecretKey;
import java.util.List;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Cors cors;
    private final OAuth2 oAuth2;
    private final Jwt jwt;

    private final String riotApiKey;

    @Getter
    @RequiredArgsConstructor
    public static class Cors {
        private final List<String> allowedOrigins;
    }

    @Getter
    @RequiredArgsConstructor
    public static class OAuth2 {
        private final List<String> authorizedRedirectUris;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Jwt {
        private final String jwtSecretKey;
        private final long accessTokenExp;
        private final long refreshTokenExp;

        public SecretKey getSecretKey() {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
        }
    }
}
