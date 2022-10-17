package com.example.siderswebapp.web.controller;

import com.example.siderswebapp.service.member.MemberService;
import com.example.siderswebapp.web.request.member.SignUpDto;
import com.example.siderswebapp.web.response.member.AuthMemberResponse;
import com.example.siderswebapp.web.response.member.DuplicateNameCheckDto;
import com.example.siderswebapp.web.response.member.MemberPostResponse;
import com.example.siderswebapp.web.response.member.SignUpMemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")  // 회원 가입
    public SignUpMemberResponse signup(@RequestBody SignUpDto signUpDto,
                                       UsernamePasswordAuthenticationToken user) {

        return memberService.signUp(signUpDto, user);
    }

    // test용
//    @GetMapping("/signup/test")  // 회원 가입
//    public SignUpMemberResponse test(@AuthenticationPrincipal OAuth2User oAuth2User) {
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//        SignUpDto signUpDto = new SignUpDto((String) attributes.get("id"));
//
//        return memberService.signUp(signUpDto, oAuth2User);
//    }

    // 아이디 중복 조회
    @GetMapping("/signup")
    public DuplicateNameCheckDto duplicateCheck(@RequestParam String name) {
        return memberService.duplicateNameCheck(name);
    }

    @GetMapping("/member")  // 회원 정보 조회 (인증 여부도 조회 가능)
    public AuthMemberResponse member(UsernamePasswordAuthenticationToken authentication) {
        return memberService.getMemberInfo(authentication);
    }

    @DeleteMapping("/member")  // 회원 탈퇴
    public void deleteMember(UsernamePasswordAuthenticationToken authentication) {
        memberService.deleteMember(authentication);
    }

    // 멤버의 게시글 조회
    @GetMapping("/member/posts")
    public MemberPostResponse memberPosts(UsernamePasswordAuthenticationToken authentication) {
        return memberService.memberPosts(authentication);
    }

}
