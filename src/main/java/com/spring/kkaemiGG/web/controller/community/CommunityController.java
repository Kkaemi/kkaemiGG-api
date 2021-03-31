package com.spring.kkaemiGG.web.controller.community;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/community")
@Controller
public class CommunityController {

    @GetMapping("/list")
    public String community() {
        return "community/list";
    }

    @GetMapping("/write")
    public String postsSave() {
        return "community/write";
    }

    @GetMapping("/view")
    public String postsView() {
        return "community/view";
    }

    @GetMapping("/edit")
    public String postsEdit() {
        return "community/edit";
    }

}
