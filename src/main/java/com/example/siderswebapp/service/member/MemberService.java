package com.example.siderswebapp.service.member;

import com.example.siderswebapp.auth.jwt.JwtProvider;
import com.example.siderswebapp.domain.member.Member;
import com.example.siderswebapp.domain.member.RoleType;
import com.example.siderswebapp.repository.member.MemberRepository;
import com.example.siderswebapp.web.request.member.SignUpDto;
import com.example.siderswebapp.web.response.member.AuthMemberResponse;
import com.example.siderswebapp.web.response.member.SignUpMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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

        Member member = Member.builder()
                .name(signUpDto.getName())
                .authId((String) attributes.get("id"))
                .email((String) attributes.get("sub"))
                .picture((String) attributes.get("picture"))
                .roleType(RoleType.USER)
                .refreshToken(refreshToken)
                .build();

        memberRepository.save(member);

        return new SignUpMemberResponse(member, accessToken);
    }

    // TODO: 토큰을 프론트에서 계속 가지고 있을 수 있다는 가정 하에 auth결과로는 accessToken을 내려주지 않음.
    public AuthMemberResponse getMemberInfo(Authentication authentication) {
        String authId = authentication.getName();

        Member member = memberRepository.findByAuthId(authId)
                .orElseThrow(IllegalAccessError::new);

        return new AuthMemberResponse(member);
    }
}
