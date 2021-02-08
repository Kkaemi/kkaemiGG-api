package com.spring.kkaemiGG.development;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiKey {

    public static String riotApiKey;
    public static String youtubeApiKey;

    @Value("${RIOT_API_KEY}")
    public void setRiotApiKey(String riotApiKey) {
        ApiKey.riotApiKey = riotApiKey;
    }

    @Value("${YOUTUBE_API_KEY}")
    public void setYoutubeApiKey(String youtubeApiKey) {
        ApiKey.youtubeApiKey = youtubeApiKey;
    }

}
