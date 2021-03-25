package com.spring.kkaemiGG.web.controller.community;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String postsView(@RequestParam String title, Model model) {
        model.addAttribute("title", title);
        return "community/view";
    }

    @GetMapping("/edit")
    public String postsEdit() {
        return "community/edit";
    }

}
