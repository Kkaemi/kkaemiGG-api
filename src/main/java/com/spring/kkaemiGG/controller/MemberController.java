package com.spring.kkaemiGG.controller;

import com.spring.kkaemiGG.dto.MemberDto;
import com.spring.kkaemiGG.entity.Member;
import com.spring.kkaemiGG.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

        model.addAttribute("memberDto", new MemberDto());

        return "registerForm";
    }

    @PostMapping("/memberRegister")
    public String processMember(@Valid MemberDto memberDto, Errors errors) {

        if (errors.hasErrors()) return "registerForm";

        Member member = memberDto.toEntity();

        memberService.save(member);

        return "redirect:/";
    }

    @PostMapping("/validateDuplicateEmail")
    @ResponseBody
    public boolean validateDuplicateEmail(@RequestParam("email") String email) {
        return memberService.validateDuplicateMember(email);
    }

}
