package com.spring.kkaemiGG.web.controller.index;

import com.google.api.services.youtube.model.SearchResult;
import com.spring.kkaemiGG.service.youtube.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final YoutubeService youtubeService;

    @GetMapping("/")
    public String index(Model model) {

        List<SearchResult> youtubeSearchResultList = youtubeService.getSearchList();

        model.addAttribute("youtubeSearchResultList", youtubeSearchResultList);

        return "index";
    }

}
