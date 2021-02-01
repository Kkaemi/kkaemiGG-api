package com.spring.kkaemiGG.controller;

import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.spring.kkaemiGG.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class RecordController {

    private final RecordService recordService;

    @GetMapping("/record")
    public ModelAndView record(@RequestParam String summonerNickName) {

        ModelAndView modelAndView = new ModelAndView();

        Summoner summoner = recordService.getSummoner(summonerNickName);

        if (!summoner.exists()) {
            modelAndView.setViewName("recordErrorPage");
            return modelAndView;
        }

        List<LeagueEntry> leagueEntryList = recordService.getLeagueEntryList(summoner);
        MatchHistory matchHistory = recordService.getMatchHistory(summoner);

        modelAndView.addObject("summoner", summoner);
        modelAndView.addObject("leagueEntryList", leagueEntryList);
        modelAndView.addObject("matchHistory", matchHistory);

        modelAndView.setViewName("record");

        return modelAndView;
    }

}
