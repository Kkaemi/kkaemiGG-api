package com.spring.kkaemiGG.service;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.summoner.Summoner;

import java.util.ArrayList;
import java.util.List;

public class RecordService {

    Queue[] queueType = {Queue.RANKED_SOLO, Queue.RANKED_FLEX};

    public Summoner getSummoner(String summonerNickName) {

        Summoner summoner = Orianna.summonerNamed(summonerNickName).get();

        return summoner;

    }

    public List<LeagueEntry> getLeagueEntryList(Summoner summoner) {

        List<LeagueEntry> leagueEntryList = new ArrayList<>();

        for (Queue queue : queueType) {

            // 랭크정보가 없으면 리스트에 리그포지션을 추가하지 않음
            if (summoner.getLeaguePosition(queue) == null) {
                continue;
            }

            leagueEntryList.add(summoner.getLeaguePosition(queue));

        }

        return leagueEntryList;

    }

    public MatchHistory getMatchHistory(Summoner summoner) {

        return summoner.matchHistory().withEndIndex(20).get();

    }
}
