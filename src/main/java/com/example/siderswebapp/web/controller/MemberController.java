package com.example.siderswebapp.web.controller;

import com.example.siderswebapp.service.member.MemberService;
import com.example.siderswebapp.web.request.member.SignUpDto;
import com.example.siderswebapp.web.response.member.AuthMemberResponse;
import com.example.siderswebapp.web.response.member.SignUpMemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")  // 회원 가입
    public SignUpMemberResponse signup(@RequestBody SignUpDto signUpDto,
                                       @AuthenticationPrincipal OAuth2User oAuth2User) {

        return memberService.signUp(signUpDto, oAuth2User);
    }

    @GetMapping("/member")  // 유저 정보를 내려줌
    public AuthMemberResponse member(Authentication authentication) {
        return memberService.getMemberInfo(authentication);
    }
}
