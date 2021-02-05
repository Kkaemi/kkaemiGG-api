package com.spring.kkaemiGG.web.controller.community;

import com.spring.kkaemiGG.service.posts.PostsService;
import com.spring.kkaemiGG.web.dto.PostsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RequestMapping("/community")
@Controller
public class CommunityController {

    private final PostsService postsService;

    @GetMapping("/community")
    public ModelAndView community() {
        return new ModelAndView("/community/community", "posts", postsService.findAllDesc());
    }

    @GetMapping("/posts-save")
    public ModelAndView postsSave() {
        return new ModelAndView("/community/posts-save", "requestDto", new PostsSaveRequestDto());
    }

}
