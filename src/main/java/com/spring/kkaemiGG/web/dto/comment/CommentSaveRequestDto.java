package com.spring.kkaemiGG.web.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentSaveRequestDto {

    private Long postId;
    private Long parentCommentId;
    private Long groupId;
    private String content;
}
