package com.spring.kkaemiGG.web.controller;

import com.spring.kkaemiGG.annotation.CustomRestController;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.service.CommentService;
import com.spring.kkaemiGG.web.dto.comment.CommentListResponseDto;
import com.spring.kkaemiGG.web.dto.comment.CommentResponseDto;
import com.spring.kkaemiGG.web.dto.comment.CommentSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CustomRestController
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/v1/posts/{postId}/comments")
    public CommentListResponseDto getPostComments(
            @PathVariable Long postId,
            Pageable pageable
    ) {
        return commentService.findByPostId(postId, pageable);
    }

    @PostMapping("/v1/comments")
    public CommentResponseDto save(
            @RequestBody CommentSaveRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        return commentService.save(user, requestDto);
    }

//    @PatchMapping("/v1/comments/{commentId}")
//    public ResponseEntity<Long> update(
//            @PathVariable Long commentId,
//            @RequestBody CommentUpdateRequestDto requestDto
//    ) throws BadRequestException {
//        return ResponseEntity.ok(commentService.update(commentId, requestDto));
//    }

    @DeleteMapping("/v1/comments/{commentId}")
    public Long deleteComment(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return commentId;
    }
}
