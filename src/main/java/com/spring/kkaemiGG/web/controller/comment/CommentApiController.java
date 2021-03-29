package com.spring.kkaemiGG.web.controller.comment;

import com.spring.kkaemiGG.config.auth.LoginUser;
import com.spring.kkaemiGG.config.auth.dto.SessionUser;
import com.spring.kkaemiGG.service.comment.CommentService;
import com.spring.kkaemiGG.web.dto.comment.CommentSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        return commentService.save(requestDto);
    }

}
