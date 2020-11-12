package com.spring.kkaemiGG.controller;

import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.spring.kkaemiGG.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RecordController {

    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/record")
    public String record(@RequestParam String summonerNickName, Model model) {

        Summoner summoner = recordService.getSummoner(summonerNickName);
//        MatchlistDTO matchlistDTO = recordService.getMatchlist(summonerDTO.getAccountId());
        List<LeagueEntry> leagueEntryList = recordService.getLeagueEntryList(summonerNickName);
//
        model.addAttribute("summoner", summoner);
//        model.addAttribute("matchlistDTO", matchlistDTO);
        model.addAttribute("leagueEntryList", leagueEntryList);
//
//        model.addAttribute("matches", matchlistDTO.getMatches());

        return "record";
    }

}
