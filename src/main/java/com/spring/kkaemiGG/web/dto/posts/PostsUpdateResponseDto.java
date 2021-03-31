package com.spring.kkaemiGG.web.dto.posts;

import com.spring.kkaemiGG.domain.posts.Posts;
import lombok.Getter;

@Getter
public class PostsUpdateResponseDto {

    private final String title;
    private final String content;

    public PostsUpdateResponseDto(Posts entity) {
        this.title = entity.getTitle();
        this.content = entity.getContent();
    }
}
