package com.spring.kkaemiGG.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Token {

    public static final String REFRESH_TOKEN_NAME = "kkaemigg_refresh_token";
    public static final int REFRESH_TOKEN_MAX_AGE = 60 * 60 * 24 * 7;

    private final String accessToken;
    private final String refreshToken;
}
