package com.spring.kkaemiGG.web.dto.posts;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Getter
@RequiredArgsConstructor
public class PostPageResponseDto {

    private final Page<PostDto> data;

    @Getter
    public static class PostDto {
        private final Long postId;
        private final String userNickname;
        private final String title;
        private final Long comments;
        private final String createdDate;

        @QueryProjection
        public PostDto(
                Long postId,
                String userNickname,
                String title,
                Long comments,
                LocalDateTime createdDate
        ) {
            this.postId = postId;
            this.userNickname = userNickname;
            this.title = title;
            this.comments = comments;
            this.createdDate = createdDate.atOffset(ZoneOffset.UTC).toString();
        }
    }
}
