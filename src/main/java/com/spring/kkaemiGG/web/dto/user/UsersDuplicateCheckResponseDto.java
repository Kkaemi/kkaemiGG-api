package com.spring.kkaemiGG.web.dto.user;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UsersDuplicateCheckResponseDto {

    private final HttpStatus status;
    private final Boolean success;

    @Builder
    public UsersDuplicateCheckResponseDto(HttpStatus status, Boolean success) {
        this.status = status;
        this.success = success;
    }

}
