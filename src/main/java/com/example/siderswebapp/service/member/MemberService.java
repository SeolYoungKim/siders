package com.example.siderswebapp.service.member;

import com.example.siderswebapp.auth.jwt.JwtProvider;
import com.example.siderswebapp.domain.member.Member;
import com.example.siderswebapp.domain.member.RoleType;
import com.example.siderswebapp.exception.MemberNotFoundException;
import com.example.siderswebapp.repository.member.MemberRepository;
import com.example.siderswebapp.web.request.member.SignUpDto;
import com.example.siderswebapp.web.response.member.AuthMemberResponse;
import com.example.siderswebapp.web.response.member.DuplicateNameCheckDto;
import com.example.siderswebapp.web.response.member.MemberPostResponse;
import com.example.siderswebapp.web.response.member.SignUpMemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public SignUpMemberResponse signUp(SignUpDto signUpDto, OAuth2User oAuth2User) {

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String accessToken = jwtProvider.generateAccessToken(oAuth2User);
        String refreshToken = jwtProvider.generateRefreshToken();

        String authId = (String) attributes.get("id");

        // TODO: 어떻게 변경할지 나중에 다시 생각. authId 중복 가입은 이미 OAuth2 filter에서 막아주고 있음.
        if (memberRepository.existsByAuthId(authId)) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        Member member = getNewMember(signUpDto, attributes, refreshToken, authId);

        memberRepository.save(member);

        return new SignUpMemberResponse(member, accessToken);
    }

    public AuthMemberResponse getMemberInfo(Authentication authentication) {
        // 멤버를 조회했는데 없으면 없다고 내려줘야 한다. 인증 없이 접근 가능한 홈 화면, 조회 화면 등에서 필요함.

        String authId = getAuthId(authentication);

        Member member = memberRepository.findByAuthId(authId)
                .orElse(null);

        return new AuthMemberResponse(member);
    }

    public void deleteMember(Authentication authentication) {
        String authId = getAuthId(authentication);

        Member member = memberRepository.findByAuthId(authId)
                .orElseThrow(MemberNotFoundException::new);

        memberRepository.delete(member);
    }

    public DuplicateNameCheckDto duplicateNameCheck(String name) {
        return new DuplicateNameCheckDto(memberRepository.existsByName(name));
    }

    public MemberPostResponse memberPosts(UsernamePasswordAuthenticationToken authentication) {

        String authId = getAuthId(authentication);

        Member member = memberRepository.findByAuthId(authId)
                .orElseThrow(MemberNotFoundException::new);

        return new MemberPostResponse(member);
    }


    private String getAuthId(Authentication authentication) {
        return authentication != null
                ? authentication.getName()
                : "";
    }

    private Member getNewMember(SignUpDto signUpDto, Map<String, Object> attributes, String refreshToken, String authId) {
        return Member.builder()
                .name(signUpDto.getName())
                .authId(authId)
                .email((String) attributes.get("sub"))
                .picture((String) attributes.get("picture"))
                .roleType(RoleType.USER)
                .refreshToken(refreshToken)
                .build();
    }

}
