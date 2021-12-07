package com.spring.kkaemiGG.web.dto.summoner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LeaguePositionResponseDto {

    private final RankInfo soloRank;
    private final RankInfo freeRank;

    @Getter
    @RequiredArgsConstructor
    public static class RankInfo {
        private final String tier;
        private final int division;
        private final int leaguePoint;
        private final int wins;
        private final int losses;
    }
}
