package com.spring.kkaemiGG.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Cors cors;
    private final OAuth2 oAuth2;

    @Getter
    @RequiredArgsConstructor
    @ConstructorBinding
    public static class Cors {
        private final List<String> allowedOrigins;
    }

    @Getter
    @RequiredArgsConstructor
    @ConstructorBinding
    public static class OAuth2 {
        private final List<String> authorizedRedirectUris;
    }
}
