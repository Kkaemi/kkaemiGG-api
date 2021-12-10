package com.spring.kkaemiGG.service;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.staticdata.Item;
import com.merakianalytics.orianna.types.core.staticdata.ReforgedRune;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.spring.kkaemiGG.util.QueueNameUtils;
import com.spring.kkaemiGG.util.RomanNumeralConvertor;
import com.spring.kkaemiGG.web.dto.summoner.LeaguePositionResponseDto;
import com.spring.kkaemiGG.web.dto.summoner.MatchInfoListResponseDto;
import com.spring.kkaemiGG.web.dto.summoner.MatchInfoResponseDto;
import com.spring.kkaemiGG.web.dto.summoner.ProfileResponseDto;
import lombok.RequiredArgsConstructor;
import no.stelar7.api.r4j.basic.constants.api.regions.LeagueShard;
import no.stelar7.api.r4j.basic.constants.api.regions.RegionShard;
import no.stelar7.api.r4j.impl.R4J;
import no.stelar7.api.r4j.impl.lol.builders.matchv5.match.MatchBuilder;
import no.stelar7.api.r4j.pojo.lol.match.v5.LOLMatch;
import no.stelar7.api.r4j.pojo.lol.match.v5.MatchParticipant;
import no.stelar7.api.r4j.pojo.lol.staticdata.perk.PerkPath;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class SummonerService {

    private final R4J r4J;

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

    public MatchInfoListResponseDto getMatchInfoList(String userName, Integer beginIndex) {
        Summoner summoner = Summoner.named(userName).get();
        MatchBuilder matchBuilder = new MatchBuilder(LeagueShard.KR);

        DecimalFormat df = new DecimalFormat("00");

        List<MatchInfoResponseDto> data = r4J.getLoLAPI().getMatchAPI()
                .getMatchList(
                        RegionShard.ASIA,
                        summoner.getPuuid(),
                        null,
                        null,
                        beginIndex,
                        20,
                        null,
                        null
                ).stream()
                .map(matchId -> {
                    LOLMatch lolMatch = matchBuilder.withId(matchId).getMatch();

                    // Queue 타입 구하기
                    String queue = QueueNameUtils.toPrettyName(lolMatch.getQueue());

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
                    MatchInfoResponseDto.ChampionInfo championInfo = new MatchInfoResponseDto.ChampionInfo(
                            "챔피언 정보 없음",
                            0,
                            null
                    );
                    Champion champion = Orianna.championWithId(searchedSummoner.getChampionId()).get();
                    if (champion.exists()) {
                        championInfo = new MatchInfoResponseDto.ChampionInfo(
                                champion.getName(),
                                searchedSummoner.getChampionLevel(),
                                champion.getImage().getURL()
                        );
                    } else {
                        champion = Orianna.championNamed(searchedSummoner.getChampionName()).withLocale("en_US").get();
                        if (champion.exists()) {
                            champion = Orianna.championWithId(champion.getId()).get();
                            championInfo = new MatchInfoResponseDto.ChampionInfo(
                                    champion.getName(),
                                    searchedSummoner.getChampionLevel(),
                                    champion.getImage().getURL()
                            );
                        }
                    }

                    // 스펠 정보
                    List<MatchInfoResponseDto.SpellInfo> spellInfoList = Orianna.summonerSpellsWithIds(
                                    searchedSummoner.getSummoner1Id(),
                                    searchedSummoner.getSummoner2Id()
                            ).get().stream()
                            .map(summonerSpell -> {
                                if (!summonerSpell.exists()) {
                                    return new MatchInfoResponseDto.SpellInfo(
                                            "스펠 정보 없음",
                                            null,
                                            null
                                    );
                                }

                                return new MatchInfoResponseDto.SpellInfo(
                                        summonerSpell.getName(),
                                        summonerSpell.getDescription(),
                                        summonerSpell.getImage().getURL()
                                );
                            })
                            .collect(Collectors.toList());

                    // 룬 정보
                    List<MatchInfoResponseDto.RuneInfo> runeInfoList = searchedSummoner.getPerks().getPerkStyles().stream()
                            .map(perkStyle -> {
                                if (perkStyle.getDescription().equals("primaryStyle")) {
                                    if (perkStyle.getSelections().get(0).getPerk() == 0) {
                                        return new MatchInfoResponseDto.RuneInfo(
                                                "룬 정보 없음",
                                                null, null
                                        );
                                    }

                                    ReforgedRune reforgedRune = Orianna.reforgedRuneWithId(perkStyle.getSelections().get(0).getPerk()).get();

                                    if (!reforgedRune.exists()) {
                                        return new MatchInfoResponseDto.RuneInfo(
                                                "룬 정보 없음",
                                                null, null
                                        );
                                    }

                                    return new MatchInfoResponseDto.RuneInfo(
                                            reforgedRune.getName(),
                                            reforgedRune.getLongDescription(),
                                            "http://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/" + reforgedRune.getImage().toLowerCase()
                                    );
                                } else {
                                    PerkPath perkPath = r4J.getDDragonAPI().getPerkPath(perkStyle.getStyle(), null, "ko_KR");

                                    if (perkPath == null) {
                                        return new MatchInfoResponseDto.RuneInfo(
                                                "룬 정보 없음",
                                                null, null
                                        );
                                    }

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
                            return new MatchInfoResponseDto.ItemInfo(
                                    "아이템 정보 없음",
                                    null,
                                    null
                            );
                        }

                        Item item = Orianna.itemWithId(itemId).get();

                        if (!item.exists()) {
                            return new MatchInfoResponseDto.ItemInfo(
                                    "아이템 정보 없음",
                                    null,
                                    null
                            );
                        }

                        return new MatchInfoResponseDto.ItemInfo(
                                item.getName(),
                                item.getDescription(),
                                item.getImage().getURL()
                        );
                    }).collect(Collectors.toList());

                    // 게임 참가자 정보
                    List<MatchInfoResponseDto.ParticipantInfo> participantInfoList = participantList.stream()
                            .map(matchParticipant -> {
                                Champion participantChampion = Orianna.championWithId(matchParticipant.getChampionId()).get();

                                if (participantChampion.exists()) {
                                    return new MatchInfoResponseDto.ParticipantInfo(
                                            matchParticipant.getSummonerName(),
                                            participantChampion.getName(),
                                            participantChampion.getImage().getURL()
                                    );
                                }

                                participantChampion = Orianna.championNamed(matchParticipant.getChampionName())
                                        .withLocale("en_US").get();

                                if (participantChampion.exists()) {
                                    participantChampion = Orianna.championWithId(participantChampion.getId()).get();
                                    return new MatchInfoResponseDto.ParticipantInfo(
                                            matchParticipant.getSummonerName(),
                                            participantChampion.getName(),
                                            participantChampion.getImage().getURL()
                                    );
                                }

                                return new MatchInfoResponseDto.ParticipantInfo(
                                        matchParticipant.getSummonerName(),
                                        "챔피언 정보 없음",
                                        null
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
                .filter(matchInfoResponseDto -> !matchInfoResponseDto.getQueue().equals("튜토리얼"))
                .collect(Collectors.toList());

        return new MatchInfoListResponseDto(data);
    }
}
