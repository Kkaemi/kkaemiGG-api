package com.spring.kkaemiGG.controller;

import com.spring.kkaemiGG.config.auth.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final HttpSession httpSession;

    @GetMapping
    public String home(Model model) {

        SessionMember member = (SessionMember) httpSession.getAttribute("member");

        if (member != null) {
            model.addAttribute("memberName", member.getName());
        }

        return "home";
    }
}
