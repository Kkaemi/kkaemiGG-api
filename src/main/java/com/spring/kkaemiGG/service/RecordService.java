package com.spring.kkaemiGG.service;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.spring.kkaemiGG.bean.LeagueEntryDTO;
import com.spring.kkaemiGG.bean.MatchlistDTO;
import com.spring.kkaemiGG.bean.SummonerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class RecordService {

    public Summoner getSummoner(String summonerNickName) {

        Summoner summoner = Orianna.summonerNamed(summonerNickName).get();

        return summoner;

    }
}
