package com.spring.kkaemiGG.web.dto.posts;

import com.spring.kkaemiGG.domain.post.Post;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.domain.view.View;
import lombok.Getter;

import java.time.ZoneOffset;

@Getter
public class PostResponseDto {

    private final Long postId;
    private final Long userId;
    private final String userNickname;
    private final Long viewCount;
    private final String title;
    private final String content;
    private final String createdDate;

    public PostResponseDto(Post post) {
        User user = post.getUser();
        this.postId = post.getId();
        this.userId = user.getId();
        this.userNickname = user.getNickname();
        this.viewCount = post.getViews().stream().mapToLong(View::getCount).sum();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdDate = post.getCreatedDate().atOffset(ZoneOffset.UTC).toString();
    }
}
