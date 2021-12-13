package com.spring.kkaemiGG.web.dto.comment;

import com.spring.kkaemiGG.domain.comment.Comment;
import com.spring.kkaemiGG.domain.user.User;
import lombok.Getter;

import java.time.ZoneOffset;

@Getter
public class CommentResponseDto {

    private final Long commentId;
    private final Long groupId;
    private final Long userId;
    private final String author;
    private final String content;
    private final String createdDate;
    private final Boolean isChildComment;

    public CommentResponseDto(Comment comment) {
        User commentUser = comment.getUser();

        this.commentId = comment.getId();
        this.groupId = comment.getParentComment() == null ? null : comment.getParentComment().getId();
        this.userId = commentUser.getId();
        this.author = commentUser.getNickname();
        this.content = comment.getContent();
        this.createdDate = comment.getCreatedDate().atOffset(ZoneOffset.UTC).toString();
        this.isChildComment = comment.getGroupOrder() > 1;
    }
}
