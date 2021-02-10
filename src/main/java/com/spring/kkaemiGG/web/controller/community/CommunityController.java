package com.spring.kkaemiGG.web.controller.community;

import com.spring.kkaemiGG.service.posts.PostsService;
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

    @GetMapping("/list")
    public ModelAndView community() {
        return new ModelAndView("/community/list", "posts", postsService.findAllDesc());
    }

    @GetMapping("/write")
    public String postsSave() {
        return "/community/write";
    }

}
