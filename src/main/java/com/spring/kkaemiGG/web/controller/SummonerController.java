package com.spring.kkaemiGG.web.controller;


import com.spring.kkaemiGG.annotation.CustomRestController;
import com.spring.kkaemiGG.service.SummonerService;
import com.spring.kkaemiGG.web.dto.summoner.ProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}