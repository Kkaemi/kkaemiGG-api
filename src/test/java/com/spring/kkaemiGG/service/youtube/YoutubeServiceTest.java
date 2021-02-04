package com.spring.kkaemiGG.service.youtube;

import com.google.api.services.youtube.model.SearchResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class YoutubeServiceTest {

    @Autowired
    YoutubeService youtubeService;

    @Test
    public void getSearchListTest() {

        //given

        //when
        List<SearchResult> searchResultList = youtubeService.getSearchList();

        //then
        assertThat(searchResultList.size()).isEqualTo(12);

    }
}