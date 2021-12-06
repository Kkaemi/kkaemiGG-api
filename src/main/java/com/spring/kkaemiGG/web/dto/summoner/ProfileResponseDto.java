package com.spring.kkaemiGG.web.dto.summoner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProfileResponseDto {

    private final int level;
    private final String name;
    private final String profileIconImageUrl;
}
