package com.spring.kkaemiGG.web.dto;

import com.spring.kkaemiGG.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostsListResponseDto {

    private final Long id;
    private final String title;
    private final String author;
    private final LocalDateTime ModifiedDate;

    public PostsListResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.ModifiedDate = entity.getModifiedDate();
    }
}
