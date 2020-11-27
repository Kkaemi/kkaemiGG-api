package com.spring.kkaemiGG.controller;

import com.spring.kkaemiGG.config.auth.dto.SessionMember;
import com.spring.kkaemiGG.dto.MemberDto;
import com.spring.kkaemiGG.entity.Member;
import com.spring.kkaemiGG.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final HttpSession httpSession;

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/loginFail")
    public String loginFail() {
        return "loginFail";
    }

    @GetMapping("/registerForm")
    public String registerForm(Model model) {

        model.addAttribute("memberDto", new MemberDto());

        return "registerForm";
    }

    @PostMapping("/memberRegister")
    public String processMember(@Valid MemberDto memberDto, Errors errors) {

        // memberDto 유효성 검사
        if (errors.hasErrors()) return "registerForm";

        memberService.join(memberDto);

        return "redirect:/";
    }

    @PostMapping("/validateDuplicateEmail")
    @ResponseBody
    public boolean validateDuplicateEmail(@RequestParam("email") String email) {
        return memberService.validateDuplicateMember(email);
    }

    @PostMapping("/validateMember")
    @ResponseBody
    public boolean validateMember(@RequestParam Map<String, String> memberInfo) {

        MemberDto memberDto = MemberDto.builder()
                .email(memberInfo.get("email"))
                .password(memberInfo.get("pass")).build();

        System.out.println(memberDto.getEmail());
        System.out.println(memberDto.getPassword());

        return memberService.validateMember(memberDto);
    }

}
