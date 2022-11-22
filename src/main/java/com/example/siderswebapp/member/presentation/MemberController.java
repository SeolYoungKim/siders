package com.example.siderswebapp.member.presentation;

import com.example.siderswebapp.member.application.MemberService;
import com.example.siderswebapp.member.application.dto.SignUpDto;
import com.example.siderswebapp.member.presentation.dto.AuthMemberResponse;
import com.example.siderswebapp.member.presentation.dto.DuplicateNameCheckDto;
import com.example.siderswebapp.member.presentation.dto.MemberPostResponse;
import com.example.siderswebapp.member.presentation.dto.SignUpMemberResponse;
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
