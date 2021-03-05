package com.spring.kkaemiGG.web.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UsersDuplicateCheckResponseDto {

    private final Integer status;
    private final String message;

    @Builder
    public UsersDuplicateCheckResponseDto(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
