package com.spring.kkaemiGG.controller;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping
    public String home() {
        return "home";
    }
}
