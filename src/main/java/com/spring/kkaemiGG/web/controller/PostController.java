package com.spring.kkaemiGG.web.controller;

import com.spring.kkaemiGG.annotation.CustomRestController;
import com.spring.kkaemiGG.annotation.RequestIP;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.exception.BadRequestException;
import com.spring.kkaemiGG.service.PostService;
import com.spring.kkaemiGG.web.dto.posts.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CustomRestController
public class PostController {

    private final PostService postService;

    @GetMapping("/v1/posts")
    public PostPageResponseDto paging(
            PostPageRequestDto requestDto,
            Pageable pageable
    ) {
        return postService.getPage(requestDto, pageable);
    }

    @PostMapping("/v1/posts")
    public Long save(
            @RequestBody PostSaveRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        return postService.save(requestDto, user);
    }

    @GetMapping("/v1/posts/{id}")
    public PostResponseDto viewPost(
            @PathVariable Long id,
            @RequestIP String ipAddress
    ) throws BadRequestException {
        return postService.viewPost(id, ipAddress);
    }

    @PatchMapping("/v1/posts/{id}")
    public Long update(
            @PathVariable Long id,
            @RequestBody PostUpdateRequestDto requestDto
    ) throws BadRequestException {
        return postService.update(id, requestDto);
    }

    @DeleteMapping("/v1/posts/{id}")
    public Long delete(@PathVariable Long id) throws BadRequestException {
        postService.delete(id);
        return id;
    }
}
