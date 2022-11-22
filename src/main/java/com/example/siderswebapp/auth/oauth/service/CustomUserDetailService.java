package com.example.siderswebapp.auth.oauth.service;

import static com.example.siderswebapp.auth.oauth.service.AttributeKeys.ID;
import static com.example.siderswebapp.member.domain.RoleType.GUEST;

import com.example.siderswebapp.member.domain.Member;
import com.example.siderswebapp.member.domain.repository.MemberRepository;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * OAuth2LoginAuthenticationFilter가 AuthenticationManager로 인증을 위임
 * AuthenticationManager가 아래의 클래스를 이용하여 인증 객체를 생성하게 됨. -> 그리고 필터에서 security context에 얘를 등록함.
 */

@Service
@RequiredArgsConstructor
public class CustomUserDetailService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        Map<String, Object> attributes = OAuth2AttributesFactory
                .getOAuth2Attributes(registrationId, oAuth2User.getAttributes())
                .getAttributes();

        Member findMember = memberRepository
                .findByAuthId((String) attributes.get(ID))
                .orElseGet(() -> Member.builder()
                        .roleType(GUEST)
                        .build());

        return new DefaultOAuth2User(Collections.singleton(
                new SimpleGrantedAuthority(findMember.getRoleTypeKey())), attributes, ID);
    }
}
