package com.example.siderswebapp.member.application;

import com.example.siderswebapp.auth.jwt.JwtProvider;
import com.example.siderswebapp.exception.domain.MemberNotFoundException;
import com.example.siderswebapp.member.application.dto.SignUpDto;
import com.example.siderswebapp.member.domain.AuthId;
import com.example.siderswebapp.member.domain.Member;
import com.example.siderswebapp.member.domain.MemberFactory;
import com.example.siderswebapp.member.domain.Name;
import com.example.siderswebapp.member.domain.repository.MemberRepository;
import com.example.siderswebapp.member.presentation.dto.AuthMemberResponse;
import com.example.siderswebapp.member.presentation.dto.DuplicateNameCheckDto;
import com.example.siderswebapp.member.presentation.dto.MemberPostResponse;
import com.example.siderswebapp.member.presentation.dto.SignUpMemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public SignUpMemberResponse signUp(SignUpDto signUpDto, UsernamePasswordAuthenticationToken user) {
        String authId = user.getName();

        // TODO: 어떻게 변경할지 나중에 다시 생각. authId 중복 가입은 이미 OAuth2 filter에서 막아주고 있음.
        if (memberRepository.existsByAuthId(new AuthId(authId))) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken();

        Member member = MemberFactory.newInstance(signUpDto, refreshToken, authId);
        memberRepository.save(member);

        return new SignUpMemberResponse(member, accessToken);
    }

    public AuthMemberResponse getMemberInfo(Authentication authentication) {
        String authId = getAuthId(authentication);

        Member member = memberRepository.findByAuthId(new AuthId(authId))
                .orElseGet(MemberFactory::guestMember);

        return new AuthMemberResponse(member);
    }

    public void deleteMember(Authentication authentication) {
        String authId = getAuthId(authentication);

        Member member = memberRepository.findByAuthId(new AuthId(authId))
                .orElseThrow(MemberNotFoundException::new);

        memberRepository.delete(member);
    }

    public DuplicateNameCheckDto duplicateNameCheck(String name) {
        return new DuplicateNameCheckDto(memberRepository.existsByName(new Name(name)));
    }

    public MemberPostResponse memberPosts(UsernamePasswordAuthenticationToken authentication) {
        String authId = getAuthId(authentication);

        Member member = memberRepository.findByAuthId(new AuthId(authId))
                .orElseThrow(MemberNotFoundException::new);

        return new MemberPostResponse(member);
    }

    // Authentication이 null일 경우 "" 반환
    private String getAuthId(Authentication authentication) {
        if (authentication != null) {
            return authentication.getName();
        }
        return "";
    }
}
