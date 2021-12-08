package com.spring.kkaemiGG.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.staticdata.Item;
import com.merakianalytics.orianna.types.core.staticdata.ReforgedRune;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.spring.kkaemiGG.util.RomanNumeralConvertor;
import com.spring.kkaemiGG.web.dto.summoner.LeaguePositionResponseDto;
import com.spring.kkaemiGG.web.dto.summoner.MatchInfoResponseDto;
import no.stelar7.api.r4j.basic.cache.impl.FileSystemCacheProvider;
import no.stelar7.api.r4j.basic.calling.DataCall;
import no.stelar7.api.r4j.basic.constants.api.regions.LeagueShard;
import no.stelar7.api.r4j.basic.constants.api.regions.RegionShard;
import no.stelar7.api.r4j.impl.R4J;
import no.stelar7.api.r4j.impl.lol.builders.matchv5.match.MatchBuilder;
import no.stelar7.api.r4j.pojo.lol.match.v5.LOLMatch;
import no.stelar7.api.r4j.pojo.lol.match.v5.MatchParticipant;
import no.stelar7.api.r4j.pojo.lol.staticdata.perk.PerkPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class SummonerServiceTest {

    @BeforeEach
    void beforeAll() {
        Orianna.setDefaultPlatform(Platform.KOREA);
        Orianna.setDefaultLocale("ko_KR");
        Orianna.setRiotAPIKey(null);
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
        R4J r4J = new R4J(null);
        DataCall.setCacheProvider(new FileSystemCacheProvider(1000 * 60 * 60));

        Summoner summoner = Summoner.named("hide on bush").get();
        MatchBuilder matchBuilder = new MatchBuilder(LeagueShard.KR);

        DecimalFormat df = new DecimalFormat("00");

        ObjectMapper objectMapper = new ObjectMapper();

        r4J.getLoLAPI().getMatchAPI()
                .getMatchList(
                        RegionShard.ASIA,
                        summoner.getPuuid(),
                        null,
                        null,
                        0,
                        20,
                        null,
                        null
                ).stream()
                .map(matchId -> {
                    LOLMatch lolMatch = matchBuilder.withId(matchId).getMatch();

                    // Queue 타입 구하기
                    String queue = lolMatch.getQueue().toString();

                    // 게임 종료 시간
                    // getGameEndAsDate 메소드 사용시 NPE 발생
                    String gameEnd = lolMatch.getGameStartAsDate()
                            .plusSeconds(lolMatch.getGameDurationAsDuration().getSeconds()).toString();

                    // 게임 플레이 시간
                    Duration gameDuration = lolMatch.getGameDurationAsDuration();
                    String gameDurationString = gameDuration.toHours() != 0
                            ? gameDuration.toHours() + "시간 " + gameDuration.toMinutes() + "분 " + gameDuration.toMinutesPart() + "초"
                            : gameDuration.toMinutes() + "분 " + gameDuration.toMinutesPart() + "초";

                    // 게임 참가자 리스트
                    List<MatchParticipant> participantList = lolMatch.getParticipants();

                    // 해당 소환사 정보 가져오기
                    MatchParticipant searchedSummoner = participantList.stream()
                            .filter(matchParticipant -> matchParticipant.getPuuid().equals(summoner.getPuuid()))
                            .findFirst()
                            .orElseThrow();

                    // 해당 소환사의 챔피언 정보
                    Champion champion = Orianna.championWithId(searchedSummoner.getChampionId()).get();
                    int championLevel = searchedSummoner.getChampionLevel();
                    String championName = champion.getName();
                    String championImageUrl = champion.getImage().getURL();

                    MatchInfoResponseDto.ChampionInfo championInfo = new MatchInfoResponseDto.ChampionInfo(
                            championName,
                            championLevel,
                            championImageUrl
                    );

                    // 스펠 정보
                    List<MatchInfoResponseDto.SpellInfo> spellInfoList = Orianna.summonerSpellsWithIds(
                                    searchedSummoner.getSummoner1Id(),
                                    searchedSummoner.getSummoner2Id()
                            ).get().stream()
                            .map(summonerSpell -> new MatchInfoResponseDto.SpellInfo(
                                    summonerSpell.getName(),
                                    summonerSpell.getDescription(),
                                    summonerSpell.getImage().getURL()
                            ))
                            .collect(Collectors.toList());

                    // 룬 정보
                    List<MatchInfoResponseDto.RuneInfo> runeInfoList = searchedSummoner.getPerks().getPerkStyles().stream()
                            .map(perkStyle -> {
                                if (perkStyle.getDescription().equals("primaryStyle")) {
                                    ReforgedRune reforgedRune = Orianna.reforgedRuneWithId(perkStyle.getSelections().get(0).getPerk()).get();

                                    return new MatchInfoResponseDto.RuneInfo(
                                            reforgedRune.getName(),
                                            reforgedRune.getLongDescription(),
                                            "http://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/" + reforgedRune.getImage().toLowerCase()
                                    );
                                } else {
                                    PerkPath perkPath = r4J.getDDragonAPI().getPerkPath(perkStyle.getStyle(), null, "ko_KR");

                                    return new MatchInfoResponseDto.RuneInfo(
                                            perkPath.getName(),
                                            null,
                                            "http://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/" + perkPath.getIcon().toLowerCase()
                                    );
                                }
                            }).collect(Collectors.toList());

                    // KDA 정보
                    int kills = searchedSummoner.getKills();
                    int deaths = searchedSummoner.getDeaths();
                    int assists = searchedSummoner.getAssists();
                    int teamTotalKill = participantList.stream()
                            .filter(matchParticipant -> matchParticipant.getTeam().equals(searchedSummoner.getTeam()))
                            .mapToInt(MatchParticipant::getKills)
                            .sum();
                    String participantKillRate = df.format((kills + assists) / (double) teamTotalKill * 100) + "%";

                    // cs 정보
                    int totalMinionsKilled = searchedSummoner.getTotalMinionsKilled();
                    float csPerMinute = Math.round((float) totalMinionsKilled / gameDuration.toMinutes() * 10) / (float) 10;

                    // 아이템 정보
                    List<MatchInfoResponseDto.ItemInfo> itemInfoList = Stream.of(
                            searchedSummoner.getItem0(), searchedSummoner.getItem1(), searchedSummoner.getItem2(), searchedSummoner.getItem6(),
                            searchedSummoner.getItem3(), searchedSummoner.getItem4(), searchedSummoner.getItem5()
                    ).map(itemId -> {
                        if (itemId == 0) {
                            return null;
                        }

                        Item item = Orianna.itemWithId(itemId).get();

                        if (!item.exists()) {
                            return null;
                        }

                        return new MatchInfoResponseDto.ItemInfo(
                                item.getName(),
                                item.getDescription(),
                                item.getImage().getURL()
                        );
                    }).collect(Collectors.toList());

                    // 참가자 정보
                    List<MatchInfoResponseDto.ParticipantInfo> participantInfoList = participantList.stream()
                            .map(matchParticipant -> {
                                Champion participantsChampion = Orianna.championWithId(matchParticipant.getChampionId()).get();
                                return new MatchInfoResponseDto.ParticipantInfo(
                                        matchParticipant.getSummonerName(),
                                        participantsChampion.getName(),
                                        participantsChampion.getImage().getURL()
                                );
                            })
                            .collect(Collectors.toList());

                    return new MatchInfoResponseDto(
                            matchId,

                            searchedSummoner.didWin(),

                            queue, gameEnd, gameDurationString,

                            kills, deaths, assists, participantKillRate,

                            totalMinionsKilled, csPerMinute,

                            championInfo,
                            spellInfoList,
                            runeInfoList,
                            itemInfoList,
                            participantInfoList
                    );
                })
                .forEach(matchInfoResponseDto -> {
                    try {
                        System.out.println(objectMapper.writeValueAsString(matchInfoResponseDto));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
    }
}