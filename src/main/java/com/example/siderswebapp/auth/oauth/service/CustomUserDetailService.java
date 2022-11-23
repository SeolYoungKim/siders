package com.example.siderswebapp.auth.oauth.service;

import static com.example.siderswebapp.auth.oauth.service.AttributeKeys.ID;

import com.example.siderswebapp.member.domain.AuthId;
import com.example.siderswebapp.member.domain.Member;
import com.example.siderswebapp.member.domain.MemberFactory;
import com.example.siderswebapp.member.domain.repository.MemberRepository;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        Map<String, Object> oauth2attributes = AttributesParser
                .parseToOAuth2Attributes(registrationId, oauth2User.getAttributes());

        log.info(String.valueOf(oauth2attributes.get(ID).getClass()));

        Member findMember = memberRepository
                .findByAuthId(new AuthId((String) oauth2attributes.get(ID)))
                .orElseGet(MemberFactory::guestMember);

        return new DefaultOAuth2User(Collections.singleton(
                new SimpleGrantedAuthority(findMember.getRoleTypeKey())), oauth2attributes, ID);
    }
}
