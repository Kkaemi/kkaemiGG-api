package com.spring.kkaemiGG.web.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UsersLoginResponseDto {

    private final Integer status;
    private final String message;
    private final String redirectUrl;

    @Builder
    public UsersLoginResponseDto(Integer status, String message, String redirectUrl) {
        this.status = status;
        this.message = message;
        this.redirectUrl = redirectUrl;
    }
}
