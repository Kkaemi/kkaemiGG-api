package com.spring.kkaemiGG.service;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.summoner.Summoner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecordService {

    public Optional<Summoner> getSummoner(String summonerNickName) {

        Summoner summoner = Orianna.summonerNamed(summonerNickName).get();

        return Optional.ofNullable(summoner);

    }

    public List<LeagueEntry> getLeagueEntryList(Summoner summoner) {

        List<LeagueEntry> leagueEntryList = new ArrayList<>();

        Queue.RANKED.forEach(
                queue -> {
                    // 존재하는 리그 포지션만 리스트에 추가
                    if (summoner.getLeaguePosition(queue) != null) {
                        leagueEntryList.add(summoner.getLeaguePosition(queue));
                    }
                }
        );

        return leagueEntryList;

    }

    public MatchHistory getMatchHistory(Summoner summoner) {

        return MatchHistory.forSummoner(summoner).withEndIndex(20).get();

    }
}
