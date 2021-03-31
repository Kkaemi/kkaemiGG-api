package com.spring.kkaemiGG.web.dto.posts;

import com.spring.kkaemiGG.domain.posts.Posts;
import com.spring.kkaemiGG.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

    private String title;
    private String content;

    @Builder
    public PostsSaveRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Posts toEntityWithUser(User user) {
        return Posts.builder()
                .hit(0L)
                .user(user)
                .title(title)
                .content(content)
                .author(user.getNickname())
                .build();
    }
}
