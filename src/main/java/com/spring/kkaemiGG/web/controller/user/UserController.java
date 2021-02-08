package com.spring.kkaemiGG.web.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/login")
    public String loginForm() {
        return "/user/login-form";
    }

    @GetMapping("/join")
    public String registerForm() {
        return "/user/register-form";
    }

}
