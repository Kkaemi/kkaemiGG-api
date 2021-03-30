package com.spring.kkaemiGG.web.dto.posts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.kkaemiGG.domain.posts.Posts;
import com.spring.kkaemiGG.web.dto.TimeCalculator;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostsResponseDto {

    private final Long postsId;
    private Long userId;
    private Long hit;
    private Integer comments;
    private String title;
    private String content;
    private String author;
    private String timeDifference;
    private boolean owner = false;

    public PostsResponseDto(Posts entity) {
        this.postsId = entity.getId();
        this.userId = entity.getUser().getId();
        this.hit = entity.getHit();
        this.comments = entity.getComments().size();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.timeDifference = TimeCalculator.untilNow(entity.getCreatedDate());
    }

    public PostsResponseDto(Long id) {
        this.postsId = id;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }
}
