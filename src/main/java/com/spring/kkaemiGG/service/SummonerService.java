package com.spring.kkaemiGG.service;

import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.spring.kkaemiGG.web.dto.summoner.ProfileResponseDto;
import org.springframework.stereotype.Service;

@Service
public class SummonerService {

    public boolean exists(String userName) {
        Summoner summoner = Summoner.named(userName).get();
        return summoner.exists();
    }

    public ProfileResponseDto getProfileByName(String userName) {
        Summoner summoner = Summoner.named(userName).get();

        return new ProfileResponseDto(
                summoner.getLevel(),
                summoner.getName(),
                summoner.getProfileIcon().getImage().getURL()
        );
    }
}
