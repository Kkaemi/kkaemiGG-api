package com.spring.kkaemiGG.web.dto.posts;

import com.spring.kkaemiGG.domain.post.Post;
import com.spring.kkaemiGG.domain.view.View;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.ZoneOffset;

@Getter
@RequiredArgsConstructor
public class PostPageResponseDto {

    private final Page<PostDto> data;

    @Getter
    public static class PostDto {
        private final Long postId;
        private final String title;
        private final String userNickname;
        private final String createdDate;
        private final Long views;

        public PostDto(Post post) {
            int comments = post.getComments().size();
            String title = post.getTitle();

            this.postId = post.getId();
            this.userNickname = post.getUser().getNickname();
            this.createdDate = post.getCreatedDate().atOffset(ZoneOffset.UTC).toString();

            this.title = comments == 0
                    ? title
                    : title + " [" + comments + "]";

            this.views = post.getViews().stream()
                    .mapToLong(View::getCount)
                    .sum();
        }
    }
}
