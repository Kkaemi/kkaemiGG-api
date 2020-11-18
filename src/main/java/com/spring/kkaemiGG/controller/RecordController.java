package com.spring.kkaemiGG.controller;

import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.spring.kkaemiGG.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class RecordController {

    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/record")
    public ModelAndView record(@RequestParam String summonerNickName) {

        Summoner summoner = recordService.getSummoner(summonerNickName);
        List<LeagueEntry> leagueEntryList = recordService.getLeagueEntryList(summoner);
        MatchHistory matchHistory = recordService.getMatchHistory(summoner);

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("record");

        modelAndView.addObject("summoner", summoner);
        modelAndView.addObject("leagueEntryList", leagueEntryList);
        modelAndView.addObject("matchHistory", matchHistory);

        return modelAndView;
    }

}
