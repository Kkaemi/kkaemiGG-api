package com.spring.kkaemiGG.service;

import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.spring.kkaemiGG.util.RomanNumeralConvertor;
import com.spring.kkaemiGG.web.dto.summoner.LeaguePositionResponseDto;
import com.spring.kkaemiGG.web.dto.summoner.ProfileResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SummonerService {

    public boolean exists(String userName) {
        Summoner summoner = Summoner.named(userName).get();
        return summoner.exists();
    }

    public ProfileResponseDto getProfileByName(String userName) {
        Summoner summoner = Summoner.named(userName).get();

        return new ProfileResponseDto(
                summoner.getLevel(),
                summoner.getName(),
                summoner.getProfileIcon().getImage().getURL()
        );
    }

    public LeaguePositionResponseDto getLeaguePositions(String userName) {
        Summoner summoner = Summoner.named(userName).get();

        LeagueEntry soloRankLeagueEntry = summoner.getLeaguePosition(Queue.RANKED_SOLO);
        LeagueEntry freeRankLeagueEntry = summoner.getLeaguePosition(Queue.RANKED_FLEX);

        LeaguePositionResponseDto.RankInfo soloRank = null;
        LeaguePositionResponseDto.RankInfo freeRank = null;


        if (soloRankLeagueEntry != null) {
            soloRank = new LeaguePositionResponseDto.RankInfo(
                    StringUtils.capitalize(soloRankLeagueEntry.getTier().toString().toLowerCase()),
                    RomanNumeralConvertor.romanToArabic(soloRankLeagueEntry.getDivision().toString()),
                    soloRankLeagueEntry.getLeaguePoints(),
                    soloRankLeagueEntry.getWins(), soloRankLeagueEntry.getLosses()
            );
        }

        if (freeRankLeagueEntry != null) {
            freeRank = new LeaguePositionResponseDto.RankInfo(
                    StringUtils.capitalize(freeRankLeagueEntry.getTier().toString().toLowerCase()),
                    RomanNumeralConvertor.romanToArabic(freeRankLeagueEntry.getDivision().toString()),
                    freeRankLeagueEntry.getLeaguePoints(),
                    freeRankLeagueEntry.getWins(), freeRankLeagueEntry.getLosses()
            );
        }

        return new LeaguePositionResponseDto(soloRank, freeRank);
    }
}
