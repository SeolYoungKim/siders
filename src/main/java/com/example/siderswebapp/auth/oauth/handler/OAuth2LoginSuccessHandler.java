package com.example.siderswebapp.auth.oauth.handler;

import com.example.siderswebapp.domain.member.Member;
import com.example.siderswebapp.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauth2User.getAttributes();
        String authId = (String) attributes.get("id");

        Optional<Member> findMember = memberRepository.findByAuthId(authId);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(UriList.FRONT_END.getUri());

        if (findMember.isEmpty()) {

            String redirectionUri = uriBuilder
                            .queryParam("loginSuccess", false)
                            .build()
                            .toUriString();

            response.sendRedirect(redirectionUri);
        } else {

            Member member = findMember.get();

            // TODO: 토큰 발급 로직 -> member의 token 필드를 변경하기 때문에 Transactional을 걸어준것

            String redirectionUri = uriBuilder
                    .queryParam("loginSuccess", true)
                    .queryParam("token", "토큰 값을 넣어야 함")
                    .build()
                    .toUriString();

            response.sendRedirect(redirectionUri);
        }
    }
}
