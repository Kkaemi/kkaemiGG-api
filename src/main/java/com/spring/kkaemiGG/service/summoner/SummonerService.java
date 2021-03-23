package com.spring.kkaemiGG.service.summoner;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SummonerService {

    public SummonerService(@Value("${RIOT_API_KEY}") String apiKey) {
        Orianna.setRiotAPIKey(apiKey);
        Orianna.setDefaultPlatform(Platform.KOREA);
        Orianna.setDefaultLocale(Platform.KOREA.getDefaultLocale());
    }

    public Summoner getSummoner(String userName) {
        return Summoner.named(userName).get();
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
