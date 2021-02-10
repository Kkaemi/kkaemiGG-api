package com.spring.kkaemiGG.web.dto.posts;

import com.spring.kkaemiGG.domain.posts.Posts;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public class PostsListResponseDto {

    private final ChronoUnit[] pivot = {ChronoUnit.YEARS,
            ChronoUnit.MONTHS,
            ChronoUnit.WEEKS,
            ChronoUnit.DAYS,
            ChronoUnit.HOURS,
            ChronoUnit.MINUTES,
            ChronoUnit.SECONDS};
    private final String[] translator = {"년", "개월", "주일", "일", "시간", "분", "초"};

    private final Long id;
    private final String title;
    private final String author;
    private final String ModifiedDate;

    public PostsListResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.ModifiedDate = calculateFromModifiedDate(entity.getModifiedDate());
    }

    private String calculateFromModifiedDate(LocalDateTime modifiedDate) {

        LocalDateTime now = LocalDateTime.now();

        long result = 0;
        int index = 0;

        for (int i=0; i<pivot.length; i++) {
            result = modifiedDate.until(now, pivot[i]);
            if (result != 0) {
                index = i;
                break;
            }
        }

        return result + " " + translator[index] + " 전";
    }

}
