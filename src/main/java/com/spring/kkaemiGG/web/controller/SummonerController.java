package com.spring.kkaemiGG.web.controller;


import com.spring.kkaemiGG.annotation.CustomRestController;
import com.spring.kkaemiGG.service.SummonerService;
import com.spring.kkaemiGG.web.dto.summoner.LeaguePositionResponseDto;
import com.spring.kkaemiGG.web.dto.summoner.MatchInfoListResponseDto;
import com.spring.kkaemiGG.web.dto.summoner.ProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@CustomRestController
public class SummonerController {

    private final SummonerService summonerService;

    @GetMapping("/v1/summoners/{userName}/exists")
    public boolean exists(@PathVariable String userName) {
        return summonerService.exists(userName);
    }

    @GetMapping("/v1/summoners/{userName}/profile")
    public ProfileResponseDto getSummonerByUserName(@PathVariable String userName) {
        return summonerService.getProfileByName(userName);
    }

    @GetMapping("/v1/summoners/{userName}/league-positions")
    public LeaguePositionResponseDto getLeaguePositions(@PathVariable String userName) {
        return summonerService.getLeaguePositions(userName);
    }

    @GetMapping("/v1/summoners/{userName}/matches")
    public MatchInfoListResponseDto getMatchList(
            @PathVariable String userName,
            @RequestParam(required = false, defaultValue = "0") Integer beginIndex
    ) {
        return summonerService.getMatchInfoList(userName, beginIndex);
    }
}
