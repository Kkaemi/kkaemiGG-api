package com.spring.kkaemiGG.config;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import lombok.RequiredArgsConstructor;
import no.stelar7.api.r4j.basic.APICredentials;
import no.stelar7.api.r4j.basic.cache.impl.FileSystemCacheProvider;
import no.stelar7.api.r4j.basic.calling.DataCall;
import no.stelar7.api.r4j.impl.R4J;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Configuration
public class SummonerConfig {

    private final AppProperties appProperties;

    @PostConstruct
    public void configuringOrianna() {
        Orianna.setDefaultLocale("ko_KR");
        Orianna.setDefaultPlatform(Platform.KOREA);
        Orianna.setRiotAPIKey(appProperties.getRiotApiKey());
    }

    @Bean
    public R4J r4J() {
        // 파일 캐시
        // ttl 2분
        DataCall.setCacheProvider(new FileSystemCacheProvider(1000 * 60 * 2));

        return new R4J(new APICredentials(appProperties.getRiotApiKey()));
    }
}
