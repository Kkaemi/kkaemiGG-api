package com.spring.kkaemiGG.web.controller;

import com.spring.kkaemiGG.annotation.CustomRestController;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.exception.BadRequestException;
import com.spring.kkaemiGG.service.CommentService;
import com.spring.kkaemiGG.web.dto.comment.CommentListResponseDto;
import com.spring.kkaemiGG.web.dto.comment.CommentSaveRequestDto;
import com.spring.kkaemiGG.web.dto.comment.CommentUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CustomRestController
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/v1/posts/{postId}/comments")
    public CommentListResponseDto getPostComments(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user
    ) {
        return commentService.findByPostId(postId, user);
    }

    @PostMapping("/v1/comments")
    public Long save(
            @RequestBody CommentSaveRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) throws BadRequestException {
        return commentService.save(user, requestDto);
    }

    @PatchMapping("/v1/comments/{commentId}")
    public ResponseEntity<Long> update(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequestDto requestDto
    ) throws BadRequestException {
        return ResponseEntity.ok(commentService.update(commentId, requestDto));
    }

    @DeleteMapping("/api/v1/comments/{commentId}")
    public Long deleteComment(@PathVariable Long commentId) throws BadRequestException {
        commentService.delete(commentId);
        return commentId;
    }
}
