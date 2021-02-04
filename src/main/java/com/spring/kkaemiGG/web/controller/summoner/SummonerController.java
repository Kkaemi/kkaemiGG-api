package com.spring.kkaemiGG.web.controller.summoner;

import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.spring.kkaemiGG.service.summoner.SummonerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class SummonerController {

    private final SummonerService summonerService;

    @GetMapping("/summoner")
    public ModelAndView summoner(@RequestParam String userName) {

        Summoner summoner = summonerService.getSummoner(userName);

        // 등록되지 않은 유저면 user-not-found 뷰로 이동
        if (!summoner.exists()) {
            return new ModelAndView("user-not-found");
        }

        List<LeagueEntry> leagueEntryList = summonerService.getLeagueEntryList(summoner);
        MatchHistory matchHistory = summonerService.getMatchHistory(summoner);

        return new ModelAndView("user-record")
                .addObject("summoner", summoner)
                .addObject("leagueEntryList", leagueEntryList)
                .addObject("matchHistory", matchHistory);
    }

}