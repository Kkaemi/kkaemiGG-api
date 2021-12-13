package com.spring.kkaemiGG.web.dto.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@RequiredArgsConstructor
@Getter
public class CommentListResponseDto {

    private final Page<CommentResponseDto> data;
}
