package com.example.siderswebapp.web.controller;

import com.example.siderswebapp.service.member.MemberService;
import com.example.siderswebapp.web.request.member.SignUpDto;
import com.example.siderswebapp.web.response.member.AuthMemberResponse;
import com.example.siderswebapp.web.response.member.DuplicateNameCheckDto;
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

    // test용
    @GetMapping("/signup/test")  // 회원 가입
    public SignUpMemberResponse test(@AuthenticationPrincipal OAuth2User oAuth2User) {

        SignUpDto signUpDto = new SignUpDto("히히");
        return memberService.signUp(signUpDto, oAuth2User);
    }

    @GetMapping("/signup")
    public DuplicateNameCheckDto duplicateCheck(@RequestParam String name) {
        return memberService.duplicateNameCheck(name);
    }

    @GetMapping("/member")  // 유저 정보를 내려줌
    public AuthMemberResponse member(Authentication authentication) {
        return memberService.getMemberInfo(authentication);
    }

    @DeleteMapping("/member")
    public void deleteMember(Authentication authentication) throws IllegalAccessException {
        memberService.deleteMember(authentication);
    }



}
