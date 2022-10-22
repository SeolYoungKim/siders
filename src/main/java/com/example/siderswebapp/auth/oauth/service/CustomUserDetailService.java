package com.example.siderswebapp.auth.oauth.service;

import com.example.siderswebapp.domain.member.Member;
import com.example.siderswebapp.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

import static com.example.siderswebapp.auth.oauth.service.AttibuteKeys.*;
import static com.example.siderswebapp.domain.member.RoleType.GUEST;

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

        Map<String, Object> attributes = OAuthAttributes
                .of(registrationId, oAuth2User.getAttributes())
                .parsedAttributes();

        Member findMember = memberRepository
                .findByAuthId((String) attributes.get(ID))
                .orElse(null);


        return new DefaultOAuth2User(
                Collections.singleton(
                        new SimpleGrantedAuthority(
                                findMember == null
                                        ? GUEST.getKey()
                                        : findMember.getRoleTypeKey())),
                attributes, ID);

    }
}
