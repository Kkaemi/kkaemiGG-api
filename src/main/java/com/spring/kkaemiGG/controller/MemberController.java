package com.spring.kkaemiGG.controller;

import com.spring.kkaemiGG.bean.MemberDTO;
import com.spring.kkaemiGG.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/registerForm")
    public String registerForm(Model model) {

        model.addAttribute("memberDTO", new MemberDTO());

        return "registerForm";
    }

    @PostMapping("/memberRegister")
    public String processMember(@Valid MemberDTO memberDTO, Errors errors) {

        if (errors.hasErrors()) return "registerForm";

        memberService.memberRegister(memberDTO);

        return "redirect:/";
    }

}
