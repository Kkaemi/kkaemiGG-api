package com.spring.kkaemiGG.service.summoner;

import com.merakianalytics.orianna.types.core.summoner.Summoner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SummonerServiceTest {

    @Autowired
    SummonerService summonerService;

    @Test
    public void getSummonerTest() {

        //given
        String existUser = "후픽하게스왑좀";
        String noExistUser = "nollam";

        //when
        Summoner summoner1 = summonerService.getSummoner(existUser);
        Summoner summoner2 = summonerService.getSummoner(noExistUser);

        //then
        assertThat(summoner1.exists()).isEqualTo(true);
        assertThat(summoner2.exists()).isEqualTo(false);

    }

}