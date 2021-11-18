package com.spring.kkaemiGG.web.dto.posts;

import lombok.Getter;

@Getter
public class PostsPageResponseDto {

    private Long id;
    private Long comments;
    private String title;
    private String author;
    private String timeDifference;
}
