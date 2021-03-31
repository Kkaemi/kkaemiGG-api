package com.spring.kkaemiGG.web.dto.comment;

import com.spring.kkaemiGG.domain.comment.Comment;
import com.spring.kkaemiGG.web.dto.TimeCalculator;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private final Long commentId;
    private final Long userId;
    private final String author;
    private final String content;
    private final String timeDifference;
    private Boolean owner = false;
    private final Boolean step;

    public CommentResponseDto(Comment entity) {
        this.commentId = entity.getId();
        this.userId = entity.getUser().getId();
        this.author = entity.getAuthor();
        this.content = confirmDeletion(entity);
        this.timeDifference = TimeCalculator.untilNow(entity.getCreatedDate());
        this.step = isReply(entity.getGroupOrder());
    }

    private String confirmDeletion(Comment comment) {
        return comment.getDeletion()
                ? "삭제된 게시물 입니다."
                : comment.getContent();
    }

    private Boolean isReply(Integer groupOrder) {
        return groupOrder > 1;
    }

    public void setOwner(Long userId) {
        if (this.userId.equals(userId)) {
            this.owner = true;
        }
    }
}
