package com.spring.kkaemiGG.web.dto.posts;

import com.spring.kkaemiGG.domain.post.Post;
import com.spring.kkaemiGG.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    private String title;
    private String content;

    public Post toEntity(User user) {
        return Post.builder(title, content).user(user).build();
    }
}
