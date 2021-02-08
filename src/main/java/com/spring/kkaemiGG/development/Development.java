package com.spring.kkaemiGG.development;

public enum Development {

    RIOT(ApiKey.riotApiKey),
    YOUTUBE(ApiKey.youtubeApiKey);

    private final String apiKey;

    Development(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }
}
