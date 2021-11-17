package com.spring.kkaemiGG.web.controller;

import com.spring.kkaemiGG.auth.LoginUser;
import com.spring.kkaemiGG.auth.dto.SessionUser;
import com.spring.kkaemiGG.service.CommentService;
import com.spring.kkaemiGG.web.dto.comment.CommentResponseDto;
import com.spring.kkaemiGG.web.dto.comment.CommentSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping("/api/v1/comments")
    public Long save(@RequestBody CommentSaveRequestDto requestDto,
                     @LoginUser SessionUser sessionUser) {

        if (sessionUser == null) {
            return 0L;
        }

        requestDto.setAuthor(sessionUser.getNickname());
        requestDto.setUserId(sessionUser.getId());

        return commentService.save(requestDto);
    }

    @GetMapping("/api/v1/comments/{postsId}")
    public List<CommentResponseDto> find(@PathVariable Long postsId,
                                         @LoginUser SessionUser sessionUser) {
        return commentService.find(postsId, sessionUser);
    }

    @DeleteMapping("/api/v1/comments/{commentId}")
    public Long deleteComment(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return commentId;
    }

    @GetMapping("/api/v1/reply")
    public Boolean reply(@LoginUser SessionUser sessionUser) {
        return sessionUser == null;
    }

}
