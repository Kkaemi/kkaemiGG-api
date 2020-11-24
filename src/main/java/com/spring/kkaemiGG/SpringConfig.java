package com.spring.kkaemiGG;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import com.spring.kkaemiGG.repository.JpaMemberRepository;
import com.spring.kkaemiGG.repository.MemberRepository;
import com.spring.kkaemiGG.service.MemberService;
import com.spring.kkaemiGG.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class SpringConfig {

    @Value("${API_KEY}")
    private String apiKey;

    private final DataSource dataSource;
    private final EntityManager em;

    @Bean
    public RecordService recordService() {
        Orianna.setRiotAPIKey(apiKey);
        Orianna.setDefaultPlatform(Platform.KOREA);
        Orianna.setDefaultLocale("ko_KR");
        return new RecordService();
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }
}
