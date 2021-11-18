package com.spring.kkaemiGG.web.dto.posts;

import com.spring.kkaemiGG.domain.post.Post;
import com.spring.kkaemiGG.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    private String title;
    private String content;

    @Builder
    public PostSaveRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post toEntityWithUser(User user) {
        return new Post(title, content);
    }
}
