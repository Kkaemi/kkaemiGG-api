package com.spring.kkaemiGG.web.dto.comment;

import com.spring.kkaemiGG.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentSaveRequestDto {

    private Long postsId;
    private Long parentCommentId;
    private String content;
    private String author;

    @Builder
    public CommentSaveRequestDto(Long postsId, Long parentCommentId, String content) {
        this.postsId = postsId;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
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
