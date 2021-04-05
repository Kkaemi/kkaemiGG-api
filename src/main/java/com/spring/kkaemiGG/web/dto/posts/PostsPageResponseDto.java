package com.spring.kkaemiGG.web.dto.posts;

import com.spring.kkaemiGG.domain.posts.Posts;
import com.spring.kkaemiGG.web.dto.TimeCalculator;
import lombok.Getter;

@Getter
public class PostsPageResponseDto {

    private final Long id;
    private final Long comments;
    private final String title;
    private final String author;
    private final String timeDifference;

    public PostsPageResponseDto(Posts entity, Long comments) {
        this.id = entity.getId();
        this.comments = comments;
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.timeDifference = TimeCalculator.untilNow(entity.getCreatedDate());
    }

}
