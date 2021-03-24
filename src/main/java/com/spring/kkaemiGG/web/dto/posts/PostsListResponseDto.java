package com.spring.kkaemiGG.web.dto.posts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.kkaemiGG.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class PostsListResponseDto {

    @JsonIgnore
    private final ChronoUnit[] pivot = {ChronoUnit.YEARS,
            ChronoUnit.MONTHS,
            ChronoUnit.WEEKS,
            ChronoUnit.DAYS,
            ChronoUnit.HOURS,
            ChronoUnit.MINUTES,
            ChronoUnit.SECONDS};

    @JsonIgnore
    private final String[] translator = {"년", "개월", "주일", "일", "시간", "분", "초"};

    private final Long id;
    private final String title;
    private final String author;
    private final String CreatedDate;

    public PostsListResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.CreatedDate = calculateFromCreatedDate(entity.getCreatedDate());
    }

    private String calculateFromCreatedDate(LocalDateTime createdDate) {

        LocalDateTime now = LocalDateTime.now();

        long result = 0;
        int index = 0;

        for (int i=0; i<pivot.length; i++) {
            result = createdDate.until(now, pivot[i]);
            if (result != 0) {
                index = i;
                break;
            }
        }

        return result + " " + translator[index] + " 전";
    }

}
