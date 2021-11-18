package com.spring.kkaemiGG.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/login")
    public String loginForm(HttpServletRequest request) {

        System.out.println();
        System.out.println("in");

        Optional.ofNullable(request.getHeader("Referer"))
                .filter(url -> !url.contains("/login"))
                .filter(url -> !url.contains("/join"))
                .ifPresent(url -> request.getSession().setAttribute("prevPage", url));

        return "user/login-form";
    }

    @GetMapping("/join")
    public String registerForm() {
        return "user/register-form";
    }

}
