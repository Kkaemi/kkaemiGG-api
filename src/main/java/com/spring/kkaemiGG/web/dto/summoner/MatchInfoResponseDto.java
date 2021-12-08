package com.spring.kkaemiGG.web.dto.summoner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class MatchInfoResponseDto {

    private final String matchId;

    private final boolean isWin;

    private final String queue;
    private final String gameEnd;
    private final String gameDuration;

    private final int kills;
    private final int deaths;
    private final int assists;
    private final String participantKillRate;

    private final int totalMinionsKilled;
    private final float csPerMinute;

    private final ChampionInfo championInfo;

    private final List<SpellInfo> spellInfoList;
    private final List<RuneInfo> runeInfoList;
    private final List<ItemInfo> itemInfoList;
    private final List<ParticipantInfo> participantInfoList;

    @RequiredArgsConstructor
    @Getter
    public static class ChampionInfo {
        private final String championName;
        private final int championLevel;
        private final String championImageUrl;
    }

    @RequiredArgsConstructor
    @Getter
    public static class RuneInfo {
        private final String runeName;
        private final String runeDescription;
        private final String runeImageUrl;
    }

    @RequiredArgsConstructor
    @Getter
    public static class SpellInfo {
        private final String spellName;
        private final String spellDescription;
        private final String spellImageUrl;
    }

    @RequiredArgsConstructor
    @Getter
    public static class ItemInfo {
        private final String itemName;
        private final String itemDescription;
        private final String itemImageUrl;
    }

    @RequiredArgsConstructor
    @Getter
    public static class ParticipantInfo {
        private final String userName;
        private final String championName;
        private final String championImageUrl;
    }
}
