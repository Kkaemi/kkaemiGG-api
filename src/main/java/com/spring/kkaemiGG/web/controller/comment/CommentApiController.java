package com.spring.kkaemiGG.web.controller.comment;

import com.merakianalytics.datapipelines.sources.Get;
import com.spring.kkaemiGG.config.auth.LoginUser;
import com.spring.kkaemiGG.config.auth.dto.SessionUser;
import com.spring.kkaemiGG.service.comment.CommentService;
import com.spring.kkaemiGG.web.dto.comment.CommentResponseDto;
import com.spring.kkaemiGG.web.dto.comment.CommentSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        return commentService.save(requestDto);
    }

    @GetMapping("/api/v1/comments/{postsId}")
    public List<CommentResponseDto> find(@PathVariable Long postsId) {
        return commentService.find(postsId);
    }

    @GetMapping("/api/v1/reply")
    public Boolean reply(@LoginUser SessionUser sessionUser) {
        return sessionUser == null;
    }

}
