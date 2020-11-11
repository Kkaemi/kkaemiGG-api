package com.spring.kkaemiGG.service;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
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

@Service
public class RecordService {

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

    public List<LeagueEntryDTO> getLeagueEntrySet(String encryptedSummonerId) {

        String url = "https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/" +
                "{encryptedSummonerId}?api_key={API_KEY}";

        Map<String, String> param = new HashMap<String, String>();
        param.put("encryptedSummonerId", encryptedSummonerId);
        param.put("API_KEY", API_KEY);

        RestTemplate restTemplate = new RestTemplate();

//        Set<LeagueEntryDTO> leagueEntrySet = restTemplate.getForObject(url, HashSet.class, param);

        ResponseEntity<List<LeagueEntryDTO>> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<LeagueEntryDTO>>() {},
                        param);

        List<LeagueEntryDTO> leagueEntryList = responseEntity.getBody();

        return leagueEntryList;
    }
}
