package com.stepupbackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

import com.stepupbackend.dto.member.MemberSignupRequest;
import com.stepupbackend.service.MemberService;

@Controller
public class AuthController {

    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("signup", new MemberSignupRequest("", "", "", ""));
        return "auth/join";
    }

    @PostMapping("/join")
    public String join(
            @Valid @ModelAttribute("signup") MemberSignupRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/join";
        }
        memberService.register(request);
        return "redirect:/login?registered";
    }
}
