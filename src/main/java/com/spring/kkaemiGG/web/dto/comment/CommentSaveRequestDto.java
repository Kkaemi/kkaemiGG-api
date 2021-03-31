package com.spring.kkaemiGG.web.dto.comment;

import com.spring.kkaemiGG.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentSaveRequestDto {

    private Long postsId;
    private Long userId;
    private Long parentCommentId;
    private String content;
    private String author;
    private String targetNickname;

    @Builder
    public CommentSaveRequestDto(Long postsId, Long parentCommentId, String content, String targetNickname) {
        this.postsId = postsId;
        this.parentCommentId = parentCommentId;
        this.content = content;
        this.targetNickname = targetNickname;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .author(author)
                .deletion(false)
                .groupOrder(1)
                .build();
    }

}
