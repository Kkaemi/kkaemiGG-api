package com.spring.kkaemiGG.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.spring.kkaemiGG.config.AppProperties;
import com.spring.kkaemiGG.util.RomanNumeralConvertor;
import com.spring.kkaemiGG.web.dto.summoner.LeaguePositionResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class SummonerServiceTest {

    @BeforeEach
    void beforeAll() {
        Orianna.setDefaultPlatform(Platform.KOREA);
        Orianna.setDefaultLocale("ko_KR");
    }

    @DisplayName("존재하는 소환사인지 확인")
    @Test
    void exists() {
        // given
        String existsUserName = "hide on bush";
        String notExistsUserName = "hausidhfiahsd";

        // when
        Summoner existsSummoner = Orianna.summonerNamed(existsUserName).get();
        Summoner notExistsSummoner = Orianna.summonerNamed(notExistsUserName).get();

        // then
        assertThat(existsSummoner.exists()).isEqualTo(true);
        assertThat(notExistsSummoner.exists()).isEqualTo(false);
    }

    @DisplayName("유저 이름으로 프로필 검색")
    @Test
    void getProfileByName() {
        String userName = "hide on bush";

        Summoner summoner = Orianna.summonerNamed(userName).get();

        System.out.println(summoner);
    }

    @DisplayName("소환사 리그 정보 검색")
    @Test
    void getLeagues() throws JsonProcessingException {
        Summoner summoner = Summoner.named("성근 왕자").get();

        LeagueEntry soloRankLeagueEntry = summoner.getLeaguePosition(Queue.RANKED_SOLO);
        LeagueEntry freeRankLeagueEntry = summoner.getLeaguePosition(Queue.RANKED_FLEX);

        LeaguePositionResponseDto.RankInfo soloRank = null;
        LeaguePositionResponseDto.RankInfo freeRank = null;

        if (soloRankLeagueEntry != null) {
            soloRank = new LeaguePositionResponseDto.RankInfo(
                    soloRankLeagueEntry.getTier().toString(),
                    RomanNumeralConvertor.romanToArabic(soloRankLeagueEntry.getDivision().toString()),
                    soloRankLeagueEntry.getLeaguePoints(),
                    soloRankLeagueEntry.getWins(), soloRankLeagueEntry.getLosses()
            );
        }

        if (freeRankLeagueEntry != null) {
            freeRank = new LeaguePositionResponseDto.RankInfo(
                    freeRankLeagueEntry.getTier().toString(),
                    RomanNumeralConvertor.romanToArabic(freeRankLeagueEntry.getDivision().toString()),
                    freeRankLeagueEntry.getLeaguePoints(),
                    freeRankLeagueEntry.getWins(), freeRankLeagueEntry.getLosses()
            );
        }

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(new LeaguePositionResponseDto(soloRank, freeRank)));
    }

    @DisplayName("매치 리스트 가져오기")
    @Test
    void getChampionInfo() {
//        DataCall.setCacheProvider(new FileSystemCacheProvider());
//
//        no.stelar7.api.r4j.pojo.lol.summoner.Summoner sum = no.stelar7.api.r4j.pojo.lol.summoner.Summoner.byName(LeagueShard.KR, "hide on bush");
//
//        MatchListBuilder builder = new MatchListBuilder();
//        builder = builder.withPuuid(sum.getPUUID()).withPlatform(LeagueShard.KR);
//
//        MatchBuilder matchBuilder = new MatchBuilder(sum.getPlatform());
//        TimelineBuilder timelineBuilder = new TimelineBuilder(sum.getPlatform());
//
//        builder.withType(MatchlistMatchType.RANKED).withQueue().withCount(5).getLazy();
    }
}