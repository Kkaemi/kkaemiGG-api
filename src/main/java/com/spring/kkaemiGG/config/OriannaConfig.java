package com.spring.kkaemiGG.config;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Configuration
public class OriannaConfig {

    private final AppProperties appProperties;

    @PostConstruct
    public void configuringOrianna() {
        Orianna.setDefaultLocale("ko_KR");
        Orianna.setDefaultPlatform(Platform.KOREA);
        Orianna.setRiotAPIKey(appProperties.getRiotApiKey());
    }
}
