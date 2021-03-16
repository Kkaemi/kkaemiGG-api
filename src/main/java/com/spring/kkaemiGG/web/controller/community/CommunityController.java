package com.spring.kkaemiGG.web.controller.community;

import com.spring.kkaemiGG.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/community")
@Controller
public class CommunityController {

    @GetMapping("/list")
    public String community() {
        return "/community/list";
    }

    @GetMapping("/write")
    public String postsSave() {
        return "/community/write";
    }

}
