package com.spring.kkaemiGG.web.dto.posts;

import com.spring.kkaemiGG.domain.posts.Posts;
import com.spring.kkaemiGG.web.dto.TimeCalculator;
import lombok.Getter;

@Getter
public class PostsListResponseDto {

    private final Long id;
    private final String title;
    private final String author;
    private final String timeDifference;

    public PostsListResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.timeDifference = TimeCalculator.untilNow(entity.getCreatedDate());
    }

}
