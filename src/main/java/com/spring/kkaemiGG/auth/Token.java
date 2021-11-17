package com.spring.kkaemiGG.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Token {

    private final String accessToken;
    private final String refreshToken;
}
