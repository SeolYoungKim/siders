package com.example.siderswebapp.auth.oauth.handler;

import com.example.siderswebapp.auth.UriList;
import com.example.siderswebapp.auth.jwt.JwtProvider;
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
import javax.servlet.http.Cookie;
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
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        Optional<Member> findMember = memberRepository.findByAuthId(getAuthId(oauth2User));

        addJwtToCookie(oauth2User, response);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(UriList.FRONT_END.getUri());

        if (findMember.isEmpty()) {
            String redirectionUri = uriBuilder
                    .queryParam("loginSuccess", false)
                    .build()
                    .toUriString();

            response.sendRedirect(redirectionUri);
            return;
        }

        Member member = findMember.get();
        member.saveRefreshToken(jwtProvider.generateRefreshToken());

        String redirectionUri = uriBuilder
                .queryParam("loginSuccess", true)
                .build()
                .toUriString();

        response.sendRedirect(redirectionUri);

    }

    private void addJwtToCookie(OAuth2User oauth2User, HttpServletResponse response) {
        final String COOKIE_NAME = "token";
        String accessToken = jwtProvider.generateAccessToken(oauth2User);

        Cookie cookie = new Cookie(COOKIE_NAME, accessToken);
        cookie.setMaxAge(3600);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    private String getAuthId(OAuth2User oauth2User) {
        Map<String, Object> attributes = oauth2User.getAttributes();
        return (String) attributes.get("id");
    }
}
