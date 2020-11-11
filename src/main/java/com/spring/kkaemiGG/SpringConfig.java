package com.spring.kkaemiGG;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import com.spring.kkaemiGG.service.RecordService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Value("${API_KEY}")
    private String apiKey;

    @Bean
    public RecordService recordService() {
        Orianna.setRiotAPIKey(apiKey);
        Orianna.setDefaultPlatform(Platform.KOREA);
        Orianna.setDefaultLocale("ko_KR");
        return new RecordService();
    }
}
