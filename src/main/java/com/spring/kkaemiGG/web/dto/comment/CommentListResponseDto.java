package com.spring.kkaemiGG.web.dto.comment;

import com.spring.kkaemiGG.domain.comment.Comment;
import com.spring.kkaemiGG.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class CommentListResponseDto {

    private final List<CommentDto> data;

    @Getter
    public static class CommentDto {
        private final Long commentId;
        private final Long userId;
        private final String author;
        private final String content;
        private final String createdDate;
        private final Boolean isOwner;
        private final Boolean isChildComment;

        public CommentDto(Comment comment, User authUser) {
            User commentUser = comment.getUser();

            this.commentId = comment.getId();
            this.userId = commentUser.getId();
            this.author = commentUser.getNickname();
            this.content = comment.getContent();
            this.createdDate = comment.getCreatedDate().atOffset(ZoneOffset.UTC).toString();
            this.isOwner = authUser != null && commentUser.getId().equals(authUser.getId());
            this.isChildComment = comment.getGroupOrder() > 1;
        }
    }
}
