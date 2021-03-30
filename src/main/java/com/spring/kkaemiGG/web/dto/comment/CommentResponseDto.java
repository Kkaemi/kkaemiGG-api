package com.spring.kkaemiGG.web.dto.comment;

import com.spring.kkaemiGG.domain.comment.Comment;
import com.spring.kkaemiGG.web.dto.TimeCalculator;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private final Long id;
    private final String author;
    private final String content;
    private final String timeDifference;
    private final Boolean step;

    public CommentResponseDto(Comment entity) {
        this.id = entity.getId();
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
}
