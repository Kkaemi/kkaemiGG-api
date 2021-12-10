package com.spring.kkaemiGG.web.dto.posts;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@RequiredArgsConstructor
public class PostPageResponseDto {

    private final Page<PostDto> data;

    @Getter
    public static class PostDto {
        private final Long postId;
        private final String title;
        private final Long comments;
        private final String userNickname;
        private final String createdDate;
        private final Long views;

        @QueryProjection
        public PostDto(
                Long postId,
                String title,
                Long comments,
                String userNickname,
                LocalDateTime createdDate,
                Long views
        ) {
            this.postId = postId;
            this.userNickname = userNickname;
            this.title = title;
            this.comments = comments;
            this.createdDate = createdDate.atOffset(ZoneOffset.UTC).toString();
            this.views = views;
        }
    }
}
