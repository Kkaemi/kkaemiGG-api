package com.spring.kkaemiGG.web.controller.posts;

import com.spring.kkaemiGG.config.auth.LoginUser;
import com.spring.kkaemiGG.config.auth.dto.SessionUser;
import com.spring.kkaemiGG.service.posts.PostsService;
import com.spring.kkaemiGG.web.dto.posts.PostsResponseDto;
import com.spring.kkaemiGG.web.dto.posts.PostsSaveRequestDto;
import com.spring.kkaemiGG.web.dto.posts.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto,
                     @LoginUser SessionUser sessionUser) {

        if (sessionUser == null) {
            return 0L;
        }

        return postsService.save(requestDto, sessionUser);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id,
                       @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

}
