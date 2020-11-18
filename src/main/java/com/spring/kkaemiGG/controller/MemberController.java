package com.spring.kkaemiGG.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    @GetMapping("/registForm")
    public String registForm() {
        return "registForm";
    }
}
