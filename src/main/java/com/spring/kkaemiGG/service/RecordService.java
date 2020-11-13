package com.spring.kkaemiGG.service;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.summoner.Summoner;

import java.util.ArrayList;
import java.util.List;

public class RecordService {

    public Summoner getSummoner(String summonerNickName) {

        Summoner summoner = Orianna.summonerNamed(summonerNickName).get();

        return summoner;

    }

    public List<LeagueEntry> getLeagueEntryList(String summonerNickName) {

        List<LeagueEntry> leagueEntryList = new ArrayList<LeagueEntry>();

        Summoner summoner = Orianna.summonerNamed(summonerNickName).get();

        Queue[] queueType = {Queue.RANKED_SOLO, Queue.RANKED_FLEX};

        for (int i = 0; i < queueType.length; i++) {

            // 랭크정보가 없으면 리스트에 리그포지션을 추가하지 않음
            if (summoner.getLeaguePosition(queueType[i]) == null) continue;
            leagueEntryList.add(summoner.getLeaguePosition(queueType[i]));

        }

        return leagueEntryList;

    }

    public MatchHistory getMatchHistory(String summonerNickName) {

        Summoner summoner = Orianna.summonerNamed(summonerNickName).get();

        return summoner.matchHistory().withEndIndex(20).get();

    }
}
