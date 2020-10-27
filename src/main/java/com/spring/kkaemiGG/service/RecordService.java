package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.bean.MatchlistDTO;
import com.spring.kkaemiGG.bean.SummonerDTO;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class RecordService {

    @Value("${API_KEY}")
    String API_KEY;

    public SummonerDTO getSummoner(String summonerNickName) {

        String url = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" +
                "{summonerNickName}?api_key={API_KEY}";

        Map<String, String> param = new HashMap<String, String>();
        param.put("summonerNickName", summonerNickName.replace(" ", ""));
        param.put("API_KEY", API_KEY);

        RestTemplate restTemplate = new RestTemplate();

        SummonerDTO summonerDTO = restTemplate.getForObject(url, SummonerDTO.class, param);

        return summonerDTO;

    }

    public MatchlistDTO getMatchlist(String accountId) {

        String url = "https://kr.api.riotgames.com/lol/match/v4/matchlists/by-account/" +
                "{accountId}?api_key={API_KEY}";

        Map<String, String> param = new HashMap<String, String>();
        param.put("accountId", accountId);
        param.put("API_KEY", API_KEY);

        RestTemplate restTemplate = new RestTemplate();

        MatchlistDTO matchlistDTO = restTemplate.getForObject(url, MatchlistDTO.class, param);

        return matchlistDTO;
    }
}
