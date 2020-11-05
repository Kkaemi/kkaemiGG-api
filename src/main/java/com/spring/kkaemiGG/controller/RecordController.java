package com.spring.kkaemiGG.controller;

import com.spring.kkaemiGG.bean.LeagueEntryDTO;
import com.spring.kkaemiGG.bean.MatchlistDTO;
import com.spring.kkaemiGG.bean.MiniSeriesDTO;
import com.spring.kkaemiGG.bean.SummonerDTO;
import com.spring.kkaemiGG.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class RecordController {

    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/record")
    public String record(@RequestParam String summonerNickName, Model model) {

        SummonerDTO summonerDTO = recordService.getSummoner(summonerNickName);
//        MatchlistDTO matchlistDTO = recordService.getMatchlist(summonerDTO.getAccountId());
        List<LeagueEntryDTO> leagueEntryList = recordService.getLeagueEntrySet(summonerDTO.getId());

        model.addAttribute("summonerDTO", summonerDTO);
//        model.addAttribute("matchlistDTO", matchlistDTO);
        model.addAttribute("leagueEntryList", leagueEntryList);

//        model.addAttribute("matches", matchlistDTO.getMatches());

        return "record";
    }

}
