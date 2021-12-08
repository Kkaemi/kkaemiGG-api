package com.spring.kkaemiGG.web.dto.summoner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class MatchInfoListResponseDto {

    private final List<MatchInfoResponseDto> data;
}
