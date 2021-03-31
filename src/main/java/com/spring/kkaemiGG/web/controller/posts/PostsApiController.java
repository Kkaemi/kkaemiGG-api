package com.spring.kkaemiGG.web.controller.posts;

import com.spring.kkaemiGG.config.auth.LoginUser;
import com.spring.kkaemiGG.config.auth.dto.SessionUser;
import com.spring.kkaemiGG.service.posts.PostsService;
import com.spring.kkaemiGG.web.dto.posts.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @GetMapping("/api/v1/posts")
    public Page<PostsListResponseDto> paging(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "recent", required = false) String sort,
                                             @RequestParam(required = false) String target,
                                             @RequestParam(required = false) String keyword) {

        PostsPageRequestDto requestDto = PostsPageRequestDto.builder()
                .page(page)
                .sort(sort)
                .target(target)
                .keyword(keyword)
                .build();

        return postsService.findByRequest(requestDto);
    }

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto,
                     @LoginUser SessionUser sessionUser) {

        if (sessionUser == null) {
            return 0L;
        }

        return postsService.save(requestDto, sessionUser);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto view(@PathVariable Long id,
                                 @LoginUser SessionUser sessionUser) {
        return postsService.findByIdWithSession(id, sessionUser);
    }

    @GetMapping("/community/edit/{id}")
    public PostsUpdateResponseDto edit(@PathVariable Long id) {
        return postsService.findById(id);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id,
                       @LoginUser SessionUser sessionUser,
                       @RequestBody PostsUpdateRequestDto requestDto) {
        if (sessionUser == null) {
            return 0L;
        }
        requestDto.setAuthor(sessionUser.getNickname());
        return postsService.update(id, requestDto);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postsService.delete(id);
        return id;
    }

}
