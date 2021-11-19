package com.spring.kkaemiGG.web.controller;

import com.spring.kkaemiGG.annotation.CustomRestController;
import com.spring.kkaemiGG.exception.BadRequestException;
import com.spring.kkaemiGG.service.PostService;
import com.spring.kkaemiGG.web.dto.posts.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@CustomRestController
@RequestMapping("/v1")
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public Page<PostsPageResponseDto> paging(@RequestParam Map<String, String> requestParam) {

        PostsPageRequestDto requestDto = PostsPageRequestDto.builder()
                .page(Integer.parseInt(requestParam.get("page")))
                .sort(requestParam.get("sort"))
                .target(requestParam.get("target").isEmpty() ? null : SearchType.valueOf(requestParam.get("target")))
                .keyword(requestParam.get("keyword"))
                .build();

        return postService.findPage(requestDto);
    }

    @PostMapping("/posts")
    public Long save(@RequestBody PostSaveRequestDto requestDto) {
        return null;
    }


    @GetMapping("/posts/{id}")
    public PostsResponseDto view(@PathVariable Long id) throws BadRequestException {
        return postService.getPostById(id);
    }

    @GetMapping("/community/edit/{id}")
    public PostsUpdateResponseDto edit(@PathVariable Long id) {
        return postService.findById(id);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id,
                       @RequestBody PostsUpdateRequestDto requestDto) {
        requestDto.setAuthor(null);
        return postService.update(id, requestDto);
    }

    @DeleteMapping("/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postService.delete(id);
        return id;
    }
}
